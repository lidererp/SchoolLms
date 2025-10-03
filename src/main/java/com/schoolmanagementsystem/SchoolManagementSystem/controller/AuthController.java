package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ErrorResponse;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.RefreshToken;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.User;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.TokenStatus;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.RefreshTokenRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.UserRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.security.CustomUserDetails;
import com.schoolmanagementsystem.SchoolManagementSystem.security.JWTRequest;
import com.schoolmanagementsystem.SchoolManagementSystem.security.JwtHelper;
import com.schoolmanagementsystem.SchoolManagementSystem.security.RefreshTokenService;
import com.schoolmanagementsystem.SchoolManagementSystem.service.EmailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.accessTokenValidityMs}")
    private long accessTokenValidityMs;

    @Value("${jwt.refreshTokenValidityMs}")
    private long refreshTokenValidityMs;


    private final UserDetailsService userDetailsService;
    private final AuthenticationManager manager;
    private final JwtHelper helper;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public AuthController(UserDetailsService userDetailsService,
                          AuthenticationManager manager,
                          JwtHelper helper,
                          RefreshTokenService refreshTokenService,
                          RefreshTokenRepository refreshTokenRepository,
                          UserRepository userRepository,
                          EmailService emailService) {
        this.userDetailsService = userDetailsService;
        this.manager = manager;
        this.helper = helper;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JWTRequest request, HttpServletResponse response, HttpServletRequest httpRequest) {

        // Authenticate user
        this.doAuthenticate(request.getEmail(), request.getPassword());

        // Get user details
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());

        Long userId = userDetails.getUser().getId();

        String userAgent = httpRequest.getHeader("User-Agent");
        String ipAddress = getClientIp(httpRequest);

        String accessToken = this.helper.generateToken(userDetails);

        RefreshToken token = refreshTokenService.create(userId, userAgent, ipAddress);
        String refreshToken = token.getToken();

        // Set access token in HttpOnly cookie

        response.addHeader("Set-Cookie",
                "accessToken=" + accessToken + "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=" + (accessTokenValidityMs / 1000));

        // Set refresh token in another HttpOnly cookie (longer expiry)
        response.addHeader("Set-Cookie",
                "refreshToken=" + refreshToken + "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=" + (refreshTokenValidityMs / 1000));

        // Return only user details (without JWT)
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", userDetails.getUser().getId());
        responseBody.put("name", userDetails.getUser().getName());
        responseBody.put("username", userDetails.getUsername());
        responseBody.put("role", userDetails.getUser().getRole());

        return ResponseEntity.ok(responseBody);

    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear both cookies

        // Optional: Invalidate refresh token in DB
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenRepository.findByToken(cookie.getValue())
                            .ifPresent(rt -> {
                                rt.setStatus(TokenStatus.REVOKED);
                                refreshTokenRepository.save(rt);
                            });
                }
            }
        }

        response.addHeader("Set-Cookie", "accessToken=; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=0");
        response.addHeader("Set-Cookie", "refreshToken=; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=0");

        return ResponseEntity.ok("Logged out successfully");

    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@CookieValue(value = "accessToken", required = false) String token) {

        if (token == null || !helper.validateToken(token)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body(Map.of("authenticated", false, "message", "Invalid or missing token"));

        }
        return ResponseEntity.ok(Map.of("authenticated", true, "message", "Authenticated"));

    }


    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);

        try {

            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {

            throw new BadCredentialsException(" Invalid Username or Password  !!");

        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {

        // 1. Extract refresh token from cookie
        String oldRefreshToken = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("No refresh token found"));

        try {

            // 2. Verify token (checks expiry + existence)
            RefreshToken token = refreshTokenService.verify(oldRefreshToken);

            // 3. Generate NEW tokens
            User user = userRepository.findById(token.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String userAgent = request.getHeader("User-Agent");
            String ipAddress = getClientIp(request);
            ;

            String accessToken = helper.generateToken(new CustomUserDetails(user));

            RefreshToken newRefreshToken = refreshTokenService.create(user.getId(), userAgent, ipAddress);

            refreshTokenService.revokeByToken(oldRefreshToken);

            // 5. Set new cookies
            response.addHeader("Set-Cookie",
                    "accessToken=" + accessToken +
                            "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=" + (accessTokenValidityMs / 1000));

            response.addHeader("Set-Cookie",
                    "refreshToken=" + newRefreshToken.getToken() +
                            "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=" + (refreshTokenValidityMs / 1000));

            return ResponseEntity.ok(Map.of(
                    "message", "Tokens refreshed",
                    "accessTokenExpiry", accessTokenValidityMs / 1000,
                    "refreshTokenExpiry", refreshTokenValidityMs / 1000
            ));

        } catch (Exception e) {

            // 6. Clear cookies if something goes wrong
            response.addHeader("Set-Cookie", "accessToken=; Max-Age=0; Path=/");
            response.addHeader("Set-Cookie", "refreshToken=; Max-Age=0; Path=/");
            return new ResponseEntity<>(
                    new ErrorResponse("Authentication Failed", "Invalid or expired session. Please log in again.",
                            LocalDateTime.now().toString(), HttpStatus.UNAUTHORIZED.value()),
                    HttpStatus.UNAUTHORIZED
            );

        }

    }


    public String getClientIp(HttpServletRequest request) {

        String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader != null) {

            // Can be a comma-separated list: client, proxy1, proxy2
            return xfHeader.split(",")[0].trim();

        }
        return request.getRemoteAddr();

    }


}
