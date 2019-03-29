package com.bankingapp.service.roleservice;

import com.bankingapp.model.login.Role;
import com.bankingapp.repository.loginrepository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Component
public class RoleService{

    @Autowired
    EntityManager entityManager;

    @Autowired
    RoleRepository roleRepository;

    public Role findRoleByUserId(int auth_user_id) {
        try {
            String sql = "Select e from " + Role.class.getName() + " e " //
                    + " Where e.auth_user_id = :auth_user_id";

            Query query = entityManager.createQuery(sql, Role.class);
            query.setParameter("auth_user_id", auth_user_id);

            return (Role) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Role> findRolesofaUser(int auth_user_id) {
        try {
            String sql = "Select e from " + Role.class.getName() + " e " //
                    + " Where e.auth_user_id = :auth_user_id";

            Query query = entityManager.createQuery(sql, Role.class);
            query.setParameter("auth_user_id", auth_user_id);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
