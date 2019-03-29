package com.bankingapp.controller.logincontroller;

import com.bankingapp.model.LogParameters;
import com.bankingapp.model.login.*;
import com.bankingapp.service.adminlogservice.AdminLogService;
import com.bankingapp.service.loginservice.SessionService;
import com.bankingapp.service.loginservice.UserService;
import com.bankingapp.service.otpservice.OtpService;
import com.bankingapp.service.roleservice.RoleService;
import com.bankingapp.utils.CryptographyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    OtpService otpService;

    @Autowired
    AdminLogService adminLogService;

    @Autowired
    LogParameters logParameters;

    @Autowired
    SessionService sessionService;

    @Autowired
    CryptographyUtil cryptographyUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@RequestBody LoginCredentials loginCredentials) {

        try {

            if(loginCredentials.getUserName().isEmpty()) {
                return new Response(false, "Username can't be empty");
            }
            if(loginCredentials.getPassword().isEmpty()) {
                return new Response(false, "Password can't be empty");
            }

            if(userService.checkForUserNameAndPassword(loginCredentials.getUserName(),
                    loginCredentials.getPassword())) {
                return new Response(true, "Logged In");
            } else {
                return new Response(false, "Invalid Credentials");
            }

        } catch (Exception e) {

        }
        return new Response(false, "Ran into Exception");
    }

    @RequestMapping(value = "/login/otp", method = RequestMethod.GET)
    public LoginResponse login(@RequestParam(name = "userName") String userName,
                               @RequestParam(name = "password") String password) {

        User user = userService.findByUserNameAndPassword(userName, password);
        System.out.println(user);
        LoginResponse loginResponse = null;
        if(user == null) {
            loginResponse.setMessgae("Login Failed, Invalid Credentials");
            return loginResponse;
        } else {

            if(sessionService.checkUserAlreadyLoggedIn(userName, password)) {
                loginResponse.setMessgae("User Already Logged In, can't access again");
                return loginResponse;
            }
            int auth_user_id = user.getAuth_user_id();
            Role role = roleService.findRoleByUserId(auth_user_id);
            adminLogService.createUserLog(auth_user_id, logParameters.LOGGING_USER);
            loginResponse = new LoginResponse(user.getFirst_name()+" "+user.getLast_name(), role.getAuth_role_id(), user.getAuth_user_id()) ;
        }
        return loginResponse;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(@RequestParam(name = "userName") String userName, @RequestParam(name = "password") String password) {

        User user = userService.findByUserNameAndPassword(userName, password);


    }
}
