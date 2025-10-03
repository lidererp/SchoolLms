package com.schoolmanagementsystem.SchoolManagementSystem.security;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.User;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Load user by username from database

        User user = userRepository.findByEmail(username)
                // if useename not avalble it will throws error
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found !!!"));
        return new CustomUserDetails(user);

    }


}
