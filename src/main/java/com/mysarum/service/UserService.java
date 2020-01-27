package com.mysarum.service;

import com.mysarum.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    public User findById(int id);

    public User findUserByEmail(String email);

    public User saveUser(User user);

    public User saveStats(User user);

    public List<User> findAll();

    public Page<User> getPaginatedUsers(Pageable pageable);

    User findUserByName(String name);
}
