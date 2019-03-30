package com.bankingapp.repository.loginrepository;

import com.bankingapp.model.login.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findById(int id);

}
