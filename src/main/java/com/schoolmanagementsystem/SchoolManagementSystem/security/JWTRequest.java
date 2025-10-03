package com.schoolmanagementsystem.SchoolManagementSystem.security;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JWTRequest {


    private String email;

    private String password;



}
