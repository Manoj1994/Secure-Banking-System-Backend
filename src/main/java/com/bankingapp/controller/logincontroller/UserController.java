package com.bankingapp.controller.logincontroller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "loginPage";
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public void login(@RequestParam(name = "userName") String userName, @RequestParam(name = "password") String password) {
        System.out.println("Username = "+userName+" "+"password = "+"password");
    }

    @RequestMapping(value = "/save1", method = RequestMethod.GET)
    public void loginTemp() {
        System.out.println("Username = "+" "+"password = "+"password");
    }

    @RequestMapping(value = "/echo/{in}", method = RequestMethod.GET)
    public String echo(@PathVariable(value = "in") final String in, @AuthenticationPrincipal final UserDetails user) {
        return "Hello " + user.getUsername() + ", you said: " + in;
    }
}
