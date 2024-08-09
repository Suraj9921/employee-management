package com.codepulse.repository;

import com.codepulse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    List<User> findAllByRole(String role);
    @Query("SELECT u FROM User u WHERE u.role = :role AND LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT(:name, '%'))")
    List<User> findAllByRoleAndFullNameStartingWithIgnoreCase(@Param("role") String role, @Param("name") String name);
}

