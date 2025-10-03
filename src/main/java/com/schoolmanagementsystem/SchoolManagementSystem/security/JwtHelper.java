package com.schoolmanagementsystem.SchoolManagementSystem.security;

import com.schoolmanagementsystem.SchoolManagementSystem.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {


    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    // ✅ Inject UserDetailsService to load user details
    public JwtHelper(UserDetailsService userDetailsService, RefreshTokenRepository refreshTokenRepository) {

        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;

    }

//    public static final long JWT_TOKEN_VALIDITY = 3 * 60;

    @Value("${jwt.accessTokenValidityMs:180000}") // default 3 mins
    private long accessTokenValidityMs;

    //    public static final long JWT_TOKEN_VALIDITY =  60;
//    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);

    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {

        return getClaimFromToken(token, Claims::getExpiration);

    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {

        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());

    }

    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();
        // Add user ID (or staff ID) to the claims
        // Assuming userDetails contains a method `getUser()` which provides the user entity
        claims.put("userId", ((CustomUserDetails) userDetails).getUser().getId()); // Modify according to your user structure
        claims.put("role", ((CustomUserDetails) userDetails).getUser().getRole());
        return doGenerateToken(claims, userDetails.getUsername());

    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidityMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {

        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    public Boolean validateToken(String token) {

        try {
            final String username = getUsernameFromToken(token);
            if (username == null || isTokenExpired(token)) {
                return false;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }

    }

    public Long getUserIdFromToken(String token) {

        Claims claims = getAllClaimsFromToken(token);
        return Long.parseLong(claims.get("userId").toString());  // Get the userId from claims

    }


}
