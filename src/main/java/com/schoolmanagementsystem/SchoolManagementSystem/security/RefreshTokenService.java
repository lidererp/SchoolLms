package com.schoolmanagementsystem.SchoolManagementSystem.security;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.RefreshToken;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.TokenStatus;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

//    private final long refreshTokenDurationMs = 4 * 60 * 60 * 1000;

//    private final long refreshTokenDurationMs = 5 * 60 * 1000; // 5 minutes

    @Value("${jwt.refreshTokenValidityMs:300000}") // default 5 mins
    private long refreshTokenDurationMs;

    @Transactional

    public RefreshToken create(Long userId, String userAgent, String ipAddress) {

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUserId(userId);
        token.setUserAgent(userAgent);
        token.setIpAddress(ipAddress);
        token.setExpiryDate(new Date(System.currentTimeMillis() + refreshTokenDurationMs));
        return refreshTokenRepository.save(token);
    }

    @Transactional
    public RefreshToken verify(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        boolean isExpired = refreshToken.getExpiryDate().before(new Date());
        boolean isNotActive = refreshToken.getStatus() != TokenStatus.ACTIVE;

        if (isExpired || isNotActive) {

            if (isExpired && refreshToken.getStatus() == TokenStatus.ACTIVE) {
                refreshToken.setStatus(TokenStatus.EXPIRED);
                refreshTokenRepository.save(refreshToken);
            }

            throw new RuntimeException("Token expired or revoked");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setStatus(TokenStatus.REVOKED);
            refreshTokenRepository.save(rt);
        });
    }
}

