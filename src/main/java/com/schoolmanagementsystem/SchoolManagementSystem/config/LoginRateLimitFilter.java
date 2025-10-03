package com.schoolmanagementsystem.SchoolManagementSystem.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {


    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {

        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)))) // 5 attempts per minute
                .build();

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/request-logout-all")
                || request.getRequestURI().equals("/user/forgot")
        ) {

            String clientIp = request.getRemoteAddr();

            Bucket bucket = ipBucketMap.computeIfAbsent(clientIp, k -> createNewBucket());

            var probe = bucket.tryConsumeAndReturnRemaining(1);

            if (probe.isConsumed()) {
                filterChain.doFilter(request, response);
            } else {
                long waitForRefillInSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Too many login attempts. Please try again after "
                        + waitForRefillInSeconds + " seconds.\"}");
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }


}

