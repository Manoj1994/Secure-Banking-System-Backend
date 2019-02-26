package com.bankingapp.service.loginservice;

import com.bankingapp.model.login.User;

import java.util.Set;

public interface UserService {

    Set<User> findByUserName(String userName);
    User findByUserEmail(String email);
    User findByUserNameAndPassword(String userName, String password);
}
