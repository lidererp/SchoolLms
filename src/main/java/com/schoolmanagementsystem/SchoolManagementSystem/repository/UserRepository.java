package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    // Fetch users with ID greater than the provided cursor ,cursor here refers to last fetched id
    List<User> findTop10ByIdGreaterThanOrderByIdDesc(Long cursor);

    List<User> findTop10ByOrderByIdDesc(); // For initial fetch

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Find user by reset token
    Optional<User> findByResetToken(String resetToken);


}
