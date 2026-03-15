package com.choco.shop.service.impl;

import com.choco.shop.entity.Role;
import com.choco.shop.entity.User;
import com.choco.shop.repository.RoleRepository;
import com.choco.shop.repository.UserRepository;
import com.choco.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_CUSTOMER").orElse(null);
        if (userRole != null) {
            roles.add(userRole);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
