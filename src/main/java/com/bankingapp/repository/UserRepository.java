package com.bankingapp.repository;

import com.bankingapp.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Set<User> findByUserName(String userName);
    User findByUserEmail(String email);
    User findByUserNameAndPassword(String userName, String password);

}
