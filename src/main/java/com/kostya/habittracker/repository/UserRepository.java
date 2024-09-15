package com.kostya.habittracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kostya.habittracker.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    User findByUsername(String username);
}
