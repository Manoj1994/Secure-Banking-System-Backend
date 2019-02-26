package com.bankingapp.repository.loginrepository;

import com.bankingapp.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    Set<User> findByUserName(String userName);
    User findByUserEmail(String email);
    User findByUserNameAndPassword(String userName, String password);

}
