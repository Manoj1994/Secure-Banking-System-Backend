package com.bankingapp.controller.requestcontroller;


import com.bankingapp.model.account.ObjectCompressor;
import com.bankingapp.model.employee.Employee;
import com.bankingapp.model.request.Request;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.requestservice.RequestService;
import com.bankingapp.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request/employee")
public class EmployeeRequestController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    RequestUtils requestUtils;

    @Autowired
    ObjectCompressor objectCompressor;

    @Autowired
    RequestService requestService;

    private static final int admin = 3;

    @RequestMapping(value = "/editDetails", method = RequestMethod.POST)
    public TransactionResponse editdetails(@RequestBody Employee employee) {

        TransactionResponse transactionResponse = new TransactionResponse();

        try {

            if(!requestUtils.validateEmail(employee.getEmail_id())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your Email is not valid! ");

                return transactionResponse;
            }
            if(employee.getEmployee_name().length() <= 5) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your name is should be atleast 5 characters long! ");

                return transactionResponse;
            }
            if(!requestUtils.validateContact(employee.getContact_no())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your contact is not valid! ");

                return transactionResponse;
            }

            String employeeString = objectCompressor.toString(employee);
            Request request=new Request();
            request.setRequesterId(employee.getEmployee_id());
            request.setRequest_type("Update Employee");
            request.setRequested_value(employeeString);
            request.setDescription("Changing Details of Employee");
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
        } catch(Exception e) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }

        return transactionResponse;
    }
}