package com.schoolmanagementsystem.SchoolManagementSystem.security;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JWTResponse {


    private String accessToken;

    private Long id;

    private String username;

    private String name;


}
