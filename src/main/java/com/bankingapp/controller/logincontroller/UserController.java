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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

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
    public Response login(HttpSession session, @RequestBody LoginCredentials loginCredentials) {

        try {
            if(loginCredentials.getUserName().isEmpty()) {
                return new Response(false, "Username can't be empty");
            }
            if(loginCredentials.getPassword().isEmpty()) {
                adminLogService.createUserLog(0, "Username = "+loginCredentials.getUserName()+" entered empty password"+" at "+ new Timestamp((System.currentTimeMillis())));;
                return new Response(false, "Password can't be empty");
            }

            if(userService.checkForUserNameAndPassword(loginCredentials.getUserName(),
                    loginCredentials.getPassword())) {

                if(sessionService.checkUserAlreadyLoggedIn(
                        loginCredentials.getUserName(),
                        loginCredentials.getPassword())) {
                    return new Response(false, "Logged In User Already Exits, Logout exising session to log in again");
                }

                User user = userService.findByUserNameAndPassword(loginCredentials.getUserName(),
                        loginCredentials.getPassword());

                int otp = otpService.generateOTP(loginCredentials.getUserName()+" "+loginCredentials.getPassword());
                System.out.println(otp);

                String message = "OTP: "+otp;

                emailService.sendOtpMessage(user.getEmail(), "Secure Banking System Login OTP ", message);
                adminLogService.createUserLog(user.getAuth_user_id(), "User id = "+user.getAuth_user_id()+" entered crendetials waiting to enter otp"+" at "+ new Timestamp((System.currentTimeMillis())));
                return new Response(true, "Logged In");
            } else {
                return new Response(false, "Invalid Credentials");
            }

        } catch (Exception e) {

        }
        return new Response(false, "Ran into Exception");
    }


    @RequestMapping(value = "/login/otp", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> loginWithOtp(HttpSession session, @RequestBody OtpLoginCredentials otpLoginCredentials, HttpServletResponse response) {

        LoginResponse loginResponse = null;
        HttpHeaders headers = new HttpHeaders();

        try {

            if(String.valueOf(otpLoginCredentials.getOtp()).isEmpty()) {

                adminLogService.createUserLog(0, "Username = "+otpLoginCredentials.getUserName()+" entered empty otp"+" at "+ new Timestamp((System.currentTimeMillis())));;
                loginResponse = new LoginResponse(false, "Otp can't be empty");
            }
            if(otpLoginCredentials.getUserName().isEmpty()) {
                adminLogService.createUserLog(0, "Username = "+otpLoginCredentials.getUserName()+" entered empty username"+" at "+ new Timestamp((System.currentTimeMillis())));;
                loginResponse = new LoginResponse(false, "Username can't be empty");
            }
            if(otpLoginCredentials.getPassword().isEmpty()) {
                adminLogService.createUserLog(0, "Username = "+otpLoginCredentials.getUserName()+" entered empty password"+" at "+ new Timestamp((System.currentTimeMillis())));;
                loginResponse = new LoginResponse(false, "Password can't be empty");
            }

            if(userService.checkForUserNameAndPassword(otpLoginCredentials.getUserName(),
                    otpLoginCredentials.getPassword())) {

                if(sessionService.checkUserAlreadyLoggedIn(
                        otpLoginCredentials.getUserName(),
                        otpLoginCredentials.getPassword())) {
                    loginResponse = new LoginResponse(false, "Logged In User Exits, Logout exising session to log in again");
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

                            session.setAttribute("id", user.getAuth_user_id());
                            response.addCookie(new Cookie("foo", "bar"));
                            response.addCookie(new Cookie("heroku-nav-data", "manoj"));

                            sessionService.createSession(
                                    otpLoginCredentials.getUserName(),
                                    otpLoginCredentials.getPassword(),
                                    user.getAuth_user_id(),
                                    role.getAuth_role_id());

                            adminLogService.createUserLog(auth_user_id, logParameters.LOGGING_USER+" at "+ new Timestamp((System.currentTimeMillis())));

                            loginResponse = new LoginResponse(true,
                                    user.getFirst_name()+" "+user.getLast_name(),
                                    role.getAuth_role_id(),
                                    user.getAuth_user_id(),
                                    "Successful Login") ;
                        }else{

                            adminLogService.createUserLog(user.getAuth_user_id(), "User id = "+user.getAuth_user_id()+" entered invalid otp "+" at "+ new Timestamp((System.currentTimeMillis())));
                            loginResponse = new LoginResponse(false, "Entered Otp is NOT valid. Please Retry!");
                        }
                    }else {
                        adminLogService.createUserLog(user.getAuth_user_id(), "User id = "+user.getAuth_user_id()+" entered invalid otp "+" at "+ new Timestamp((System.currentTimeMillis())));
                        loginResponse = new LoginResponse(false, "Entered Otp is NOT valid. Please Retry!");
                    }
                }else {
                    adminLogService.createUserLog(user.getAuth_user_id(), "User id = "+user.getAuth_user_id()+" entered invalid otp "+" at "+ new Timestamp((System.currentTimeMillis())));
                    loginResponse = new LoginResponse(false, "Entered Otp is NOT valid. Please Retry!");
                }
            } else {
                adminLogService.createUserLog(0, "User name = "+otpLoginCredentials.getUserName()+" entered invalid crendentials "+" at "+ new Timestamp((System.currentTimeMillis())));
                loginResponse = new LoginResponse(false, "Invalid Credentials");
            }

        } catch(Exception e) {
            loginResponse = new LoginResponse(false, "Ran into Exception");
        }
        adminLogService.createUserLog(0, "User name = "+otpLoginCredentials.getUserName()+" ran into exception "+" at "+ new Timestamp((System.currentTimeMillis())));

        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Set-Cookie","key="+"value");

        HttpCookie cookie = ResponseCookie.from("heroku-nav-data", "manoj")
                //.path("/")
                .build();

        ResponseEntity<LoginResponse> rp = ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);

        System.out.println(rp);
        return rp;
        //return new ResponseEntity<LoginResponse>(loginResponse,headers1, HttpStatus.OK);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Response logout(HttpSession httpSession, @RequestBody LogoutObj obj) {

        try {
            sessionService.deleteById(obj.getId());
            httpSession.removeAttribute("id");
            adminLogService.createUserLog(obj.getId(), "User id = "+obj.getId()+" entered invalid otp "+" at "+ new Timestamp((System.currentTimeMillis())));;
            return new Response(true, "Logged Out Successfully");
        } catch(Exception e) {

        }
        adminLogService.createUserLog(obj.getId(), "User id = "+obj.getId()+" entered invalid otp "+" at "+ new Timestamp((System.currentTimeMillis())));;
        return new Response(true, "Logged Out Successfully");
    }
}
