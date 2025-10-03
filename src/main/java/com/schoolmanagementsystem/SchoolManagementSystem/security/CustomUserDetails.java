package com.schoolmanagementsystem.SchoolManagementSystem.security;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {


    private User user;  // This will wrap the User entity

    public CustomUserDetails(User user) {

        this.user = user;

    }

    public User getUser() {

        return user;  // Returns the User object wrapped inside CustomUserDetails

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // Manually add the "ROLE_" prefix if your roles are stored without it
        return AuthorityUtils.createAuthorityList("ROLE_" + user.getRole());

    }

    @Override
    public String getPassword() {

        return user.getPassword();  // Return the password from the User entity

    }

    @Override
    public String getUsername() {

        return user.getEmail();  // Return the email as the username

    }

    @Override
    public boolean isAccountNonExpired() {

        return true;

    }

    @Override
    public boolean isAccountNonLocked() {

        return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;

    }

    @Override
    public boolean isEnabled() {

        return true;

    }


}


