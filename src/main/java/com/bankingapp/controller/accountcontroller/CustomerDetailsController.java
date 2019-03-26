package com.bankingapp.controller.accountcontroller;

import com.bankingapp.model.account.Customer;
import com.bankingapp.service.customerservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerDetailsController {

    @Autowired
    CustomerService customerService;

    @RequestMapping("getAllCustomerDetails")
    public List<Customer> getAllCustomerDetails() {

        List<Customer> customerList = customerService.getAllCustomers();
        return customerList;

    }

    @RequestMapping("/getCustomerDetails")
    public Customer UserDetailsContoller(@RequestParam(name = "customerId") int customerId){
        try{
            Customer customer = customerService.getCustomer(customerId);
            return customer;
        }catch(Exception e){
            throw new RuntimeException();
        }
    }
}
