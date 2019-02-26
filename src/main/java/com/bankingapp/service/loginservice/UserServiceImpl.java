package com.bankingapp.service.loginservice;

import com.bankingapp.model.login.User;
import com.bankingapp.repository.loginrepository.UserRepository;
import com.bankingapp.service.loginservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private EntityManager entityManager;

    private UserRepository userRepository;

    public Set<User> findByUserName(String userName) {
        try {
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.userName = :userName ";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("userName", userName);

            return new HashSet<User>(query.getResultList());
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByUserEmail(String email) {
        try {
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.email = :email ";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("email", email);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByUserNameAndPassword(String userName, String password) {
        try {
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.userName = :userName " +" and e.password = :password";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("userName", userName);
            query.setParameter("password", password);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void saveUser(User user) {

        userRepository.save(user);
    }
}