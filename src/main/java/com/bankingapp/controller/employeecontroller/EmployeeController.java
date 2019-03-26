package com.bankingapp.controller.employeecontroller;
import com.bankingapp.configuration.AppConfig;
import com.bankingapp.model.account.Customer;
import com.bankingapp.model.employee.Employee;
import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.AccountBalanceService;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.service.accountservice.AccountUpdateService;
import com.bankingapp.service.customerservice.CustomerAccountService;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.transactionservice.TransactionRequestService;
import com.bankingapp.utils.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    AppConfig appConfig;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CustomerAccountService customerAccountService;

    @Autowired
    AccountCheckService accountCheckService;

    @Autowired
    AmountUtils amountUtils;

    @Autowired
    AccountBalanceService accountBalanceService;

    @Autowired
    TransactionRequestService transactionRequestService;

    @Autowired
    AccountUpdateService accountUpdateService;


    @RequestMapping("/getAllEmployees")
    public List<Employee> getAllEmployees() {

        List<Employee> employeeList = new ArrayList<>();
        try{

            employeeList = employeeService.getEmployeeAccounts();

            return employeeList;

        }catch(Exception e){
        }

        return employeeList;
    }

    @RequestMapping("/getAllEmployee")
    public Employee getAllEmployees(int employee_id) {

        try{
            return employeeService.getEmployeeAccount(employee_id);

        }catch(Exception e){
        }

        return null;
    }

    @RequestMapping("/getCustomerAccounts")
    public List<Customer> getAllUserAccounts() {

        List<Customer> customers = new ArrayList<>();
        try{
            customers = customerAccountService.getAllCustomers();
            return customers;

        }catch(Exception e){
        }

        return customers;
    }

    @RequestMapping("/getEmployeeAccounts")
    public List<Employee> getAllEmployeeAccounts() {

        List<Employee> employees = new ArrayList<>();
        try{
            employees = employeeService.getEmployeeAccounts();
            return employees;

        }catch(Exception e){
        }

        return employees;
    }

    @RequestMapping("/TransferMoneyFromAccount")
    public TransactionResponse transferMoneyToSavingsAccount(@RequestParam("from_account_no") int from_account_no,
                                                             @RequestParam("to_account_no") int to_account_no,
                                                             @RequestParam("amount") String amount,
                                                             @RequestParam("routing_no") int routing_no,
                                                             @RequestParam("employee_id") int employee_id,
                                                             @RequestParam("employee_type") String employee_type)
    {
        TransactionResponse transactionResponse = new TransactionResponse();
        try{

            if (!accountCheckService.checkAccountExists(from_account_no)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid payer account chosen!");
                return transactionResponse;
            }

            if (!accountCheckService.checkAccountExistsWithRoutingNo(to_account_no, routing_no)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid routing number for this account!");
                return transactionResponse;
            }

            if (!accountCheckService.checkAccountExists(to_account_no)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid payee account chosen!");
                return transactionResponse;
            }

            if (!amountUtils.isValidAmount(amount)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid amount!");
                return transactionResponse;
            }

            Double doubleAmount = Double.parseDouble(amount);
            Double balance = accountBalanceService.getBalance(from_account_no);

            if (doubleAmount > balance) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Insufficient Balance!");
                return transactionResponse;
            }

            System.out.println("Balance = "+balance);
            boolean status = false;

            int empployeeId = 2;

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            TransactionRequest transactionRequest = new TransactionRequest();

            transactionRequest.setFrom_account(from_account_no);
            transactionRequest.setTo_account(to_account_no);

            transactionRequest.setCreated_by(from_account_no);
            transactionRequest.setStatus_id(1);
            transactionRequest.setCreated_at(timestamp);
            transactionRequest.setTransaction_amount(doubleAmount);

            if(doubleAmount >= appConfig.getCriticalAmount()) {
                empployeeId = employeeService.getTierEmployeeId(2);
                transactionRequest.setCritical(true);
            } else {
                empployeeId = employeeService.getTierEmployeeId(1);
                transactionRequest.setCritical(false);
            }

            transactionRequest.setApproved_by(empployeeId);

            System.out.println("Transaction Request = "+transactionRequest);
            status = transactionRequestService.saveTransactionRequest(transactionRequest);

            if(!status) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Internal Server Error!");
                return transactionResponse;
            } else {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Your transaction request is Pending");
            }

        }catch(Exception e){

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your payment was rejected." +
                    " Ran into Exceptiom!");
        }

        return transactionResponse;

    }


}
