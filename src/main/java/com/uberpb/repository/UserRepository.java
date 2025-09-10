package com.uberpb.repository;

import com.uberpb.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User update(User user);
    boolean deleteById(int id);
    boolean existsByEmail(String email);
}
