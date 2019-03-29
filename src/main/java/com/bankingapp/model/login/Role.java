package com.bankingapp.model.login;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "auth_user_role")
@IdClass(Role.class)
public class Role implements Serializable {

    @Id
    @Column(name = "auth_user_id", updatable = false, nullable = false)
    private int auth_user_id;

    @Id
    @Column(name = "auth_role_id", updatable = false, nullable = false)
    private int auth_role_id;

    public int getAuth_user_id() {
        return auth_user_id;
    }

    public void setAuth_user_id(int auth_user_id) {
        this.auth_user_id = auth_user_id;
    }

    public int getAuth_role_id() {
        return auth_role_id;
    }

    public void setAuth_role_id(int auth_role_id) {
        this.auth_role_id = auth_role_id;
    }

}
