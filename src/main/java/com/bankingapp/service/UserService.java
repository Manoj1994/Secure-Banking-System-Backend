package com.bankingapp.service;

import com.bankingapp.model.User;

import java.util.Set;

public interface UserService {

    Set<User> findByUserName(String userName);
    User findByUserEmail(String email);
    User findByUserNameAndPassword(String userName, String password);
}
