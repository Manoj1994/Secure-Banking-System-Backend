package com.bankingapp.repository.loginrepository;

import com.bankingapp.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email =:email")
    User findUserByEmail(@Param("email") String email);

    User findByEmailAndPassword(String email, String password);

}
