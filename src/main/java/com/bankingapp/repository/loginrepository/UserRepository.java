package com.bankingapp.repository.loginrepository;

import com.bankingapp.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Set<User> findByEmail(String email);
    User findByEmailAndPassword(String email, String password);

}
