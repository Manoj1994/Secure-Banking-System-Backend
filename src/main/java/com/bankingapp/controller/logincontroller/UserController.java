package com.bankingapp.controller.logincontroller;

import com.bankingapp.model.login.LoginResponse;
import com.bankingapp.model.login.Role;
import com.bankingapp.model.login.User;
import com.bankingapp.service.loginservice.UserService;
import com.bankingapp.service.roleservice.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "loginPage";
    }

//    @RequestMapping(value = "/save", method = RequestMethod.GET)
//    public void save(@RequestParam(name = "userName") String userName, @RequestParam(name = "password") String password,
//                     @RequestParam(name = "id") Long id, @RequestParam(name = "role") String role) {
//        System.out.println("Username = "+userName+" "+"password = "+password);
//        User user = new User(id, userName, password, role);
//        userService.saveUser(user);
//    }

    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public LoginResponse login(@RequestParam(name = "userName") String userName, @RequestParam(name = "password") String password) {
        System.out.println("Username = "+userName+" "+"password = "+password);

        User user = userService.findByUserNameAndPassword(userName, password);

        LoginResponse loginResponse = null;

        if(user == null) {
            return loginResponse;
        } else {

            int auth_user_id = user.getAuth_user_id();
            Role role = roleService.findRoleByUserId(auth_user_id);

            loginResponse = new LoginResponse(user.getFirst_name(), user.getAuth_user_id(), role.getAuth_role_id());
        }

        System.out.println("Retrieved user "+user);

        return loginResponse;
    }
}
