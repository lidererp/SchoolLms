package com.schoolmanagementsystem.SchoolManagementSystem.security;

import com.schoolmanagementsystem.SchoolManagementSystem.utility.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json");

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("message", "Access Denied");
        errorBody.put("details", authException.getMessage());
        errorBody.put("timestamp", LocalDateTime.now().toString());
        errorBody.put("statusCode", HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(JsonUtil.toJson(errorBody));

    }


}
