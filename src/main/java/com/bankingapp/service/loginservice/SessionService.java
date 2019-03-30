package com.bankingapp.service.loginservice;

import com.bankingapp.model.login.Session;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.loginrepository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    EntityManager entityManager;

    public boolean createSession(String username,
                                 String password,
                                 int id,
                                 int role_id) {

        try {
            Session session = new Session();
            session.setUsername(username);
            session.setPassword(password);
            session.setAccess_right(role_id);
            session.setAccess_key(String.valueOf(id));
            session.setStatus(true);

        } catch(Exception e) {

        }
        return false;
    }

    public boolean checkUserAlreadyLoggedIn(String username, String password) {

        try {
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.userName = :userName and e.password = :password and e.status = :status";
            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            query.setParameter("status", true);

            if(query.getResultList().size() > 0) {
                return true;
            } else {
                return false;
            }

        } catch(Exception e) {

        }
        return false;
    }

    public boolean check(String username, String password, String token) {

        return true;
    }
}
