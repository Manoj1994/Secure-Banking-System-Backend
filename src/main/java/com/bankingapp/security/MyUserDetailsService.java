package com.bankingapp.security;

import com.bankingapp.model.login.Role;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.loginrepository.RoleRepository;
import com.bankingapp.repository.loginrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository authUserRepository;

    @Autowired
    private RoleRepository authUserRoleRepository;

    @Autowired
    private HttpServletRequest request;

    public MyUserDetailsService() {
        super();
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        try {
            final User auth_user = authUserRepository.findUserByEmail(email);
            if (auth_user == null) {
                throw new UsernameNotFoundException("No user found with username: " + email);
            }

            return new org.springframework.security.core.userdetails.User(auth_user.getEmail(), auth_user.getPassword(), true, true, true, true, Collections.emptyList());
        } catch (final Exception e) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }
    }
    private final List<GrantedAuthority> getGrantedAuthorities(final Set<Role> roles) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }
}
