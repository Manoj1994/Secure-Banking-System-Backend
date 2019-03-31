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
import java.sql.Timestamp;
import java.util.List;

@Component
public class SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    EntityManager entityManager;

    public boolean createSession(String username,
                                 String password,
                                 int id,
                                 int role_id,
                                 String access_key) {

        try {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            Session session = new Session();
            session.setUsername(username);
            session.setPassword(password);
            session.setAccess_right(role_id);
            session.setId(id);
            session.setTimestamp_created(timestamp);
            session.setAccess_key(access_key);
            session.setStatus(true);

            System.out.println(session);

            try {
                sessionRepository.save(session);
            } catch(Exception e) {
                e.printStackTrace();
            }

        } catch(Exception e) {

        }
        return false;
    }

    public boolean checkUserAlreadyLoggedIn(String username, String password) {

        try {
            String sql = "Select e from " + Session.class.getName() + " e " //
                    + " Where e.username = :username and e.password = :password and e.status = :status";
            Query query = entityManager.createQuery(sql, Session.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            query.setParameter("status", true);

            if(query.getResultList().size()== 1) {
                return true;
            } else {
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAcceessKey(int id, String given_access_key) {

        try {
            String sql = "Select e from " + Session.class.getName() + " e " //
                    + " Where e.id = :id";
            Query query = entityManager.createQuery(sql, Session.class);
            query.setParameter("id", id);

            if(query.getResultList().size()== 1) {

                Session session = (Session) query.getSingleResult();
                if(session.getAccess_key().equals(given_access_key)) {
                    return true;
                }
                return false;
            } else {
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteById(int id) {

        try{
            Session session = sessionRepository.findById(id);
            sessionRepository.delete(session);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkAnyusersExists() {
        try {
            if(sessionRepository.findAll().size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {

        }
        return false;
    }

    public boolean deleteInvalidRows() {
        try {
            List<Session> sessionList = sessionRepository.findAll();

            for(Session session : sessionList) {
                Timestamp created = session.getTimestamp_created();
                Timestamp current = new Timestamp(System.currentTimeMillis());

                long seconds = (current.getTime() - created.getTime())/1000;

                if((seconds % 3600) % 60 >= 3) {
                    sessionRepository.delete(session);
                }
            }
        } catch(Exception e) {

        }
        return false;
    }

    public boolean check(int id) {
        try {
            return sessionRepository.existsById(Long.valueOf(id));
        } catch(Exception e) {

        }
        return false;
    }
}
