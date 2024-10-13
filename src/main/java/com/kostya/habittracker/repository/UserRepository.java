package com.kostya.habittracker.repository;

import org.springframework.data.repository.Repository;

import com.kostya.habittracker.entity.User;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User, Integer> {
    
    User save(User user);
    
    User findByUsername(String username);
}
