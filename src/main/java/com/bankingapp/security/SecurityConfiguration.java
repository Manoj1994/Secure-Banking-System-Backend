package com.bankingapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher()
    {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin().authenticationDetailsSource(authenticationDetailsSource);
        http.authorizeRequests()
                // URLs matching for access rights
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/home/**").hasAnyAuthority("ADMIN", "TIER1", "TIER2", "USER", "MERCHANT")
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/tier1/**").hasAnyAuthority("TIER1")
                .antMatchers("/tier2/**").hasAnyAuthority("TIER2")
                .antMatchers("/user/**").hasAnyAuthority("USER")
                .antMatchers("/employee/**").hasAnyAuthority("ADMIN","TIER2")
                .antMatchers("log/**").hasAnyAuthority("ADMIN")
                .antMatchers("help/list/**").hasAnyAuthority("ADMIN")
                .antMatchers("/merchant/**").hasAnyAuthority("MERCHANT")
                .antMatchers("account-request/**").hasAnyAuthority("TIER1","ADMIN")
                .antMatchers("/otp/generateOtp/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // form login
                .csrf().disable().formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                //.defaultSuccessUrl("/merchant")

                .usernameParameter("email")
                .passwordParameter("password")
                .and()
                // logout
                .logout()
                //.addLogoutHandler(new CustomLogoutHandler())
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
