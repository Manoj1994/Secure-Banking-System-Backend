package com.bankingapp.controller.logincontroller;

import com.bankingapp.model.LogParameters;
import com.bankingapp.model.login.*;
import com.bankingapp.service.adminlogservice.AdminLogService;
import com.bankingapp.service.loginservice.SessionService;
import com.bankingapp.service.loginservice.UserService;
import com.bankingapp.service.otpservice.EmailService;
import com.bankingapp.service.otpservice.OtpService;
import com.bankingapp.service.roleservice.RoleService;
import com.bankingapp.utils.CryptographyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    EmailService emailService;

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

                if(sessionService.checkUserAlreadyLoggedIn(
                        loginCredentials.getUserName(),
                        loginCredentials.getPassword())) {
                    return new Response(false, "Logged In User Exits, Logout exising session to log in again");
                }

                User user = userService.findByUserNameAndPassword(loginCredentials.getUserName(),
                        loginCredentials.getPassword());

                int otp = otpService.generateOTP(loginCredentials.getUserName()+" "+loginCredentials.getPassword());
                System.out.println(otp);

                String message = "OTP: "+otp;

                emailService.sendOtpMessage(user.getEmail(), "Secure Banking System Login OTP ", message);
                return new Response(true, "Logged In");
            } else {
                return new Response(false, "Invalid Credentials");
            }

        } catch (Exception e) {

        }
        return new Response(false, "Ran into Exception");
    }

    @RequestMapping(value = "/login/otp", method = RequestMethod.POST)
    public LoginResponse loginWithOtp(@RequestBody OtpLoginCredentials otpLoginCredentials) {

        try {

            if(String.valueOf(otpLoginCredentials.getOtp()).isEmpty()) {
                return new LoginResponse(false, "Otp can't be empty");
            }
            if(otpLoginCredentials.getUserName().isEmpty()) {
                return new LoginResponse(false, "Username can't be empty");
            }
            if(otpLoginCredentials.getPassword().isEmpty()) {
                return new LoginResponse(false, "Password can't be empty");
            }

            if(userService.checkForUserNameAndPassword(otpLoginCredentials.getUserName(),
                    otpLoginCredentials.getPassword())) {

                if(sessionService.checkUserAlreadyLoggedIn(
                        otpLoginCredentials.getUserName(),
                        otpLoginCredentials.getPassword())) {
                    return new LoginResponse(false, "Logged In User Exits, Logout exising session to log in again");
                }

                int otp = otpLoginCredentials.getOtp();
                User user = userService.findByUserNameAndPassword(otpLoginCredentials.getUserName(),
                        otpLoginCredentials.getPassword());

                if(otpLoginCredentials.getOtp() >= 0){
                    int serverOtp = otpService.getOtp(otpLoginCredentials.getUserName()+" "+otpLoginCredentials.getPassword());

                    if(serverOtp > 0){
                        if(otp == serverOtp){
                            otpService.clearOTP(otpLoginCredentials.getUserName()+" "+otpLoginCredentials.getPassword());
                            int auth_user_id = user.getAuth_user_id();
                            Role role = roleService.findRoleByUserId(auth_user_id);

                            sessionService.createSession(
                                    otpLoginCredentials.getUserName(),
                                    otpLoginCredentials.getPassword(),
                                    user.getAuth_user_id(),
                                    role.getAuth_role_id());

                            adminLogService.createUserLog(auth_user_id, logParameters.LOGGING_USER+" at ");

                            return new LoginResponse(true,
                                    user.getFirst_name()+" "+user.getLast_name(),
                                    role.getAuth_role_id(),
                                    user.getAuth_user_id(),
                                    "Successful Login") ;
                        }else{
                            return new LoginResponse(false, "Entered Otp is NOT valid. Please Retry!");
                        }
                    }else {
                        return new LoginResponse(false, "Entered Otp is NOT valid. Please Retry!");
                    }
                }else {
                    return new LoginResponse(false, "Entered Otp is NOT valid. Please Retry!");
                }
            } else {
                return new LoginResponse(false, "Invalid Credentials");
            }

        } catch(Exception e) {

        }
        return new LoginResponse(false, "Ran into Exception");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Response logout(@RequestBody LogoutObj obj) {

        try {
            sessionService.deleteById(obj.getId());
            return new Response(true, "Logged Out Successfully");
        } catch(Exception e) {

        }
        return new Response(true, "Logged Out Successfully");
    }
}
