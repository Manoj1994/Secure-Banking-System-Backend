package com.bankingapp.controller.accountcontroller;

import com.bankingapp.model.account.Customer;
import com.bankingapp.service.customerservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/customer")
public class CustomerDetailsController {

    @Autowired
    CustomerService customerService;

    @RequestMapping("/userdetails")
    public ModelAndView UserDetailsContoller(HttpServletRequest request, String userId){
        try{

            Customer customer = customerService.getCustomer(userId);

            ModelAndView model = new ModelAndView("");
            model.addObject("user",customer);
            return model;
        }catch(Exception e){
            throw new RuntimeException();
        }

    }
}
