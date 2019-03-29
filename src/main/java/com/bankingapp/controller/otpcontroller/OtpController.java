package com.bankingapp.controller.otpcontroller;

import com.bankingapp.service.otpservice.EmailService;
import com.bankingapp.service.otpservice.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class OtpController {

    @Autowired
    public OtpService otpService;

    @Autowired
    public EmailService emailService;

    @GetMapping("/generateOtp")
    public String generateOtp(){

        String username="arihant";

        int otp = otpService.generateOTP(username);
        System.out.println(otp);

        String message = "Username:"+username+"\n OTP:"+otp;

        emailService.sendOtpMessage("sinhaarihant@gmail.com", "Secure Banking System Login OTP ", message);

        return "otppage";
    }

    @RequestMapping(value ="/validateOtp", method = RequestMethod.GET)
    public @ResponseBody String validateOtp(@RequestParam("otpnum") int otpnum){

        String username="arihant";



        //Validate the Otp
        if(otpnum >= 0){
            int serverOtp = otpService.getOtp(username);

            if(serverOtp > 0){
                if(otpnum == serverOtp){
                    otpService.clearOTP(username);
                    return ("Entered Otp is valid");
                }else{
                    return "Entered Otp is valid";
                }
            }else {
                return "Entered Otp is NOT valid. Please Retry!";
            }
        }else {
            return "Entered Otp is NOT valid. Please Retry!";
        }
    }
}
