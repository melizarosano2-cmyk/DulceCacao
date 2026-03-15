package com.choco.shop.service;

import com.choco.shop.entity.User;

public interface EmailService {
    void sendVerificationEmail(User user, String token);

    void sendPasswordResetEmail(User user, String token);
}
