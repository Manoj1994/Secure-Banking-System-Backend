package com.bankingapp.service.loginservice;

import com.bankingapp.model.login.Role;
import com.bankingapp.model.login.RoleType;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.loginrepository.UserRepository;
import com.bankingapp.service.roleservice.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    public User findByUsername(String username) {
        try {
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.userName = :userName ";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("username", username);

            User user = (User) query.getSingleResult();

            List<Role> roles = roleService.findRolesofaUser(user.getAuth_user_id());
            List<RoleType>  roleTypes = new ArrayList<>();

            for(Role role : roles) {
                if(role.getAuth_role_id() == 1) {
                    roleTypes.add(RoleType.USER);
                }
                else if(role.getAuth_role_id() == 2) {
                    roleTypes.add(RoleType.TIER1);
                }
                else if(role.getAuth_role_id() == 3) {
                    roleTypes.add(RoleType.TIER2);
                }
                else if(role.getAuth_role_id() == 4) {
                    roleTypes.add(RoleType.ADMIN);
                }
                else if(role.getAuth_role_id() == 5) {
                    roleTypes.add(RoleType.MERCHANT);
                }
            }
            user.setRoles(roleTypes);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {

        }
        return null;
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

            //String encryptedPassword = EncrytedPasswordUtils.encrptPassword(password);
            //System.out.println(encryptedPassword);
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.username = :username " +" and e.password = :password";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            System.out.println(query.getResultList());

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Boolean checkForUserNameAndPassword(String username, String password) {
        try {

            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.username = :username " +" and e.password = :password";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            if(query.getResultList().size() == 1) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException e) {

        }
        return false;
    }

//    public void saveUser(User user) {
//
//        String encryptedPassword = EncrytedPasswordUtils.encrptPassword(user.getPassword());
//        user.setPassword(encryptedPassword);
//
//        System.out.println(user);
//        userRepository.save(user);
//    }
}
