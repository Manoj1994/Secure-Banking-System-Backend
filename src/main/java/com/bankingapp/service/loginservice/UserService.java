package com.bankingapp.service.loginservice;

import com.bankingapp.model.login.User;
import com.bankingapp.repository.loginrepository.UserRepository;
import com.bankingapp.utils.EncrytedPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    public Set<User> findByUserName(String username) {
        try {
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.userName = :userName ";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("username", username);

            return new HashSet<User>(query.getResultList());
        } catch (NoResultException e) {
            return null;
        }
    }

//    public User findByUserEmail(String email) {
//        try {
//            String sql = "Select e from " + User.class.getName() + " e " //
//                    + " Where e.email = :email ";
//
//            Query query = entityManager.createQuery(sql, User.class);
//            query.setParameter("email", email);
//
//            return (User) query.getSingleResult();
//        } catch (NoResultException e) {
//            return null;
//        }
//    }

    public User findByUserNameAndPassword(String username, String password) {
        try {
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.userName = :userName " +" and e.password = :password";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("userName", username);
            query.setParameter("password", password);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void saveUser(User user) {

        String encryptedPassword = EncrytedPasswordUtils.encrptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        System.out.println(user);
        userRepository.save(user);
    }
}
