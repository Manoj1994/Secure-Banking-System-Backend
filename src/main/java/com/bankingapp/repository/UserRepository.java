package com.bankingapp.repository;

import com.bankingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);
    User findByUserEmail(String email);
    User findByUserNameAndPassword(String userName, String password);

}
