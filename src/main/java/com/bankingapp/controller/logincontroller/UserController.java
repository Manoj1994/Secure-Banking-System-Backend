package com.bankingapp.controller.logincontroller;

import com.bankingapp.model.login.LoginResponse;
import com.bankingapp.model.login.Role;
import com.bankingapp.model.login.User;
import com.bankingapp.service.loginservice.UserService;
import com.bankingapp.service.roleservice.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public LoginResponse login(ServletResponse response, @RequestParam(name = "userName") String userName, @RequestParam(name = "password") String password) {

        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, x-requested-with, Cache-Control");
        System.out.println("Username = "+userName+" "+"password = "+password);

        User user = userService.findByUserNameAndPassword(userName, password);

        LoginResponse loginResponse = null;

        if(user == null) {
            return loginResponse;
        } else {

            int auth_user_id = user.getAuth_user_id();
            Role role = roleService.findRoleByUserId(auth_user_id);

            loginResponse = new LoginResponse(user.getFirst_name()+" "+user.getLast_name(), role.getAuth_role_id(), user.getAuth_user_id()) ;
        }

        System.out.println("Retrieved user "+user);

        return loginResponse;
    }
}
