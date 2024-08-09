package com.codepulse.service;

import com.codepulse.model.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    User findByEmail(String email);
    User findById(int id);
    List<User> findAllByRole(String role);
    User updateUser(User user);
    void deleteUserById(int id);
    List<User> searchUser(String role, String name);
}

