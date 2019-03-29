package com.bankingapp.model.login;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {
    ADMIN, USER, TIER1, TIER2, MERCHANT;

    public String getAuthority() {
        return name();
    }

}
