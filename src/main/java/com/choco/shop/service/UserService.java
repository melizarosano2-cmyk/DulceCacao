package com.choco.shop.service;

import com.choco.shop.entity.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
