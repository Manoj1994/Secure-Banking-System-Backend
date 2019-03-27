package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.CustomerCompressor;
import com.bankingapp.model.employee.Employee;
import com.bankingapp.model.request.Request;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.requestservice.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request/customer")
public class CustomerRequestController {

    @Autowired
    RequestService requestService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CustomerCompressor customerCompressor;

    private int admin = 3;

    @RequestMapping(value = "/customerEditRequest", method = RequestMethod.GET)
    public TransactionResponse customerEditAccountRequest(@RequestParam("requester_id") int user_id, @RequestParam("name") String name, @RequestParam("contact") String contact, @RequestParam("address") String address, @RequestParam("email") String email)
    {
        TransactionResponse transactionResponse = new TransactionResponse();

        Customer customer = customerService.getCustomer(user_id);

        if(customer == null) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Customer with account id is not available! ");
        }

        try{

            Customer newCustomer = new Customer();
            customer.setAddress(address);
            customer.setContact(contact);
            customer.setEmail(email);
            customer.setName(name);

            if(customer.equals(newCustomer)) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! You didn't modify any details! ");
            }

            String customerString = customerCompressor.toString(customer);

            Request request=new Request();
            request.setRequesterId(user_id);
            request.setRequest_type("Update Customer");
            request.setRequested_value(customerString);
            request.setDescription("Changing Details of Customer");
            request.setStatus("Pending");
            request.setApproverId(admin);

            System.out.println(request.toString());

            if (!requestService.add_new_request(request)) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your request is not valid!");
                return transactionResponse;
            }
            else{
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("The request was successfully added to list of Pending requests");
            }
        }catch(Exception e){

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has." + " ran into Exception!");
        }
        return transactionResponse;
    }
}
