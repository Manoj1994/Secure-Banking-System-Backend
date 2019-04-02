package com.bankingapp.controller.accountcontroller;

import com.bankingapp.configuration.AppConfig;
import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.Customer;
import com.bankingapp.model.login.LoginResponse;
import com.bankingapp.model.login.Role;
import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.model.transaction.EmailTransactionParams;
import com.bankingapp.model.transaction.OtpEmailTransactionParams;
import com.bankingapp.model.transaction.TransactionParameters;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.AccountBalanceService;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.service.accountservice.AccountDetailsService;
import com.bankingapp.service.accountservice.AccountUpdateService;
import com.bankingapp.service.adminlogservice.AdminLogService;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.loginservice.SessionService;
import com.bankingapp.service.otpservice.EmailService;
import com.bankingapp.service.otpservice.OtpService;
import com.bankingapp.service.transactionservice.TransactionRequestService;
import com.bankingapp.utils.AmountUtils;
import com.bankingapp.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerTransactionController {

    @Autowired
    AppConfig appConfig;

    @Autowired
    AccountCheckService accountCheckService;

    @Autowired
    AmountUtils amountUtils;

    @Autowired
    AccountBalanceService accountBalanceService;

    @Autowired
    TransactionRequestService transactionRequestService;

    @Autowired
    AccountDetailsService accountDetailsService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    RequestUtils requestUtils;

    @Autowired
    AccountUpdateService accountUpdateService;

    @Autowired
    TransactionParameters transactionParameters;

    @Autowired
    SessionService sessionService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EmailService emailService;

    @Autowired
    OtpService otpService;

    private int admin = 3;

    @Autowired
    AdminLogService adminLogService;

    @RequestMapping("/TransferMoneyFromAccount")
    public TransactionResponse transferMoneyToSavingsAccount(@RequestParam("from_account_no") int from_account_no,
                                                             @RequestParam("to_account_no") int to_account_no,
                                                             @RequestParam("amount") String amount,
                                                             @RequestParam("routing_no") int routing_no,
                                                             @RequestParam("customer_id") int customer_id)
            throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try{

            if (!accountCheckService.checkAccountExists(customer_id, from_account_no)) {

                adminLogService.createUserLog(customer_id, "transaction request was rejected.Invalid payer account chosen!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid payer account chosen!");
                return transactionResponse;
            }

            if (!accountCheckService.checkAccountExistsWithRoutingNo(to_account_no, routing_no)) {

                adminLogService.createUserLog(customer_id, "transaction request was rejected. Invalid routing number for this account!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid routing number for this account!");
                return transactionResponse;
            }

            if (!accountCheckService.checkAccountExists(0, to_account_no)) {

                adminLogService.createUserLog(customer_id, "transaction request was rejected. Invalid payee account chosen!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid payee account chosen!");
                return transactionResponse;
            }

            if(from_account_no == to_account_no) {

                adminLogService.createUserLog(customer_id, "transaction request was rejected. from and to accounts can't be same!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " from and to accounts can't be same!");
                return transactionResponse;

            }

            if (!amountUtils.isValidAmount(amount)) {

                adminLogService.createUserLog(customer_id, "transaction request was rejected. Invalid amount!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid amount!");
                return transactionResponse;
            }

            Double doubleAmount = Double.parseDouble(amount);
            Double balance = accountBalanceService.getBalance(from_account_no);

            if (doubleAmount > balance) {

                adminLogService.createUserLog(customer_id, "transaction request was rejected. Insufficient Balance!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Insufficient Balance!");
                return transactionResponse;
            }

            System.out.println("Balance = "+balance);
            boolean status = false;

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            TransactionRequest transactionRequest = new TransactionRequest();

            transactionRequest.setFrom_account(from_account_no);
            transactionRequest.setTo_account(to_account_no);

            transactionRequest.setCreated_by(customer_id);
            transactionRequest.setRequest_type(1);
            transactionRequest.setRequest_description("Funds Transfer");
            transactionRequest.setStatus_id(1);
            transactionRequest.setCreated_at(timestamp);
            transactionRequest.setTransaction_amount(doubleAmount);

            transactionRequest.setApproved_by(admin);

            if(doubleAmount >= appConfig.getCriticalAmount()) {
                transactionRequest.setCritical(true);
            } else {
                transactionRequest.setCritical(false);
            }

            System.out.println("Transaction Request = "+transactionRequest);
            status = transactionRequestService.saveTransactionRequest(transactionRequest);

            if(!status) {

                adminLogService.createUserLog(customer_id, "transaction request was rejected.Internal Server Error!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Internal Server Error!");
                return transactionResponse;
            } else {

                adminLogService.createUserLog(customer_id, "transaction request is Pending");
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Your transaction request is Pending");
            }

        }catch(Exception e){

            e.printStackTrace();

            adminLogService.createUserLog(customer_id, "Payment was rejected. Ran into Exception");
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your payment was rejected." +
                    " Ran into Exception!");
        }

        return transactionResponse;

    }

    @RequestMapping(value = "/TransferMoneyFromAccountViaEmail", method = RequestMethod.POST)
    public TransactionResponse transferMoneyToSavingsAccountViaEmail1(@RequestBody EmailTransactionParams emailTransactionParams)
            throws Exception {
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try {

            if (!accountCheckService.checkAccountExists(0, emailTransactionParams.getAccount_no())) {

                adminLogService.createUserLog(0, "transaction request was rejected.Invalid payer account chosen!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid payer account chosen!");
                return transactionResponse;
            }

            if (!amountUtils.isValidAmount(emailTransactionParams.getAmount().toString())) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Invalid amount!");
                return transactionResponse;
            }

            if (!requestUtils.validateEmail(emailTransactionParams.getEmail())) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Invalid email!");
                return transactionResponse;
            }


            Double doubleAmount = emailTransactionParams.getAmount();
            Double balance = accountBalanceService.getBalance(emailTransactionParams.getAccount_no());

            if (doubleAmount > balance) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Insufficient Balance!");
                return transactionResponse;
            }
            boolean customerStatus = false;
            customerStatus = customerService.checkCustomerExistsWithEmail(emailTransactionParams.getEmail());
            if(!customerStatus) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Customer with given email didn't exist");
                return transactionResponse;
            }
            try {

                Account account = accountDetailsService.getAccount(emailTransactionParams.getAccount_no());
                Customer customer = customerService.getCustomer(account.getUser_id());

                int otp = otpService.generateOTP(
                        String.valueOf(customer.getUser_id()+" "+
                                customer.getEmail()+" "+
                                emailTransactionParams.getAmount()+" $$$$"));

                String message = "OTP: "+otp;
                emailService.sendOtpMessage(customer.getEmail(), "Secure Banking System Email Transfer OTP ", message);

                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Otp Sent");
            } catch (Exception e) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Ran into Exception!");
            }
            return transactionResponse;
        } catch(Exception e) {

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your payment was rejected." +
                    " Ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/TransferMoneyFromAccountViaEmailOtp", method = RequestMethod.POST)
    public TransactionResponse transferMoneyToSavingsAccountViaEmail2(
            @RequestBody OtpEmailTransactionParams otpEmailTransactionParams)
            throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try{

            if(String.valueOf(otpEmailTransactionParams.getOtp()).isEmpty()) {

                adminLogService.createUserLog(0, "Account = "+otpEmailTransactionParams.getAccount_no()+" entered empty otp"+" at "+ new Timestamp((System.currentTimeMillis())));;
                //loginResponse = new LoginResponse(false, "Otp can't be empty");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Otp can't be empty!");
            }
            if (!accountCheckService.checkAccountExists(0, otpEmailTransactionParams.getAccount_no())) {

                adminLogService.createUserLog(0, "transaction request was rejected.Invalid payer account chosen!");
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid payer account chosen!");
                return transactionResponse;
            }

            if (!amountUtils.isValidAmount(otpEmailTransactionParams.getAmount().toString())) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Invalid amount!");
                return transactionResponse;
            }

            if (!requestUtils.validateEmail(otpEmailTransactionParams.getEmail())) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Invalid email!");
                return transactionResponse;
            }

            Double doubleAmount = otpEmailTransactionParams.getAmount();
            Double balance = accountBalanceService.getBalance(otpEmailTransactionParams.getAccount_no());

            if (doubleAmount > balance) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Insufficient Balance!");
                return transactionResponse;
            }

            Account account = accountDetailsService.getAccount(otpEmailTransactionParams.getAccount_no());
            Customer customer = customerService.getCustomer(account.getUser_id());
            Customer toCustomer = customerService.getCustomerByEmail(otpEmailTransactionParams.getEmail());

            List<Account> accounts = customerService.getAllAccounts(toCustomer.getUser_id());
            int toAccountNo = -1;

            for(Account account1: accounts) {
                if(account1.getAccount_type() == 2) {
                    toAccountNo = account1.getAccount_no();
                }
            }

            if(toAccountNo == -1) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " No checking account exist with this email!");
                return transactionResponse;
            }

            if(otpEmailTransactionParams.getOtp() >= 0){
                int serverOtp = otpService.
                        getOtp(
                                String.valueOf(customer.getUser_id()+" "+
                                        customer.getEmail()+" "+
                                        otpEmailTransactionParams.getAmount()+" $$$$"));

                if(serverOtp > 0){
                    if(otpEmailTransactionParams.getOtp() == serverOtp){
                        otpService.clearOTP(String.valueOf(customer.getUser_id()+" "+
                                customer.getEmail()+" "+
                                otpEmailTransactionParams.getAmount()+" $$$$"));

                        System.out.println("Balance = "+balance);
                        boolean status = false;

                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        TransactionRequest transactionRequest = new TransactionRequest();

                        transactionRequest.setFrom_account(otpEmailTransactionParams.getAccount_no());
                        transactionRequest.setTo_account(toAccountNo);

                        transactionRequest.setCreated_by(otpEmailTransactionParams.getAccount_no());
                        transactionRequest.setStatus_id(1);
                        transactionRequest.setRequest_type(transactionParameters.TRANSFER);
                        transactionRequest.setRequest_description("Funds Transfer Via Email");
                        transactionRequest.setCreated_at(timestamp);
                        transactionRequest.setTransaction_amount(doubleAmount);
                        transactionRequest.setApproved_by(admin);
                        if(doubleAmount >= appConfig.getCriticalAmount()) {
                            transactionRequest.setCritical(true);
                        } else {
                            transactionRequest.setCritical(false);
                        }
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
                       return transactionResponse;
                    }else{
                        adminLogService.createUserLog(customer.getUser_id(), "User id = "+customer.getUser_id()+" entered invalid otp "+" at "+ new Timestamp((System.currentTimeMillis())));
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! Your payment was rejected." +
                                " Invalid Otp!");
                    }
                }else {
                    adminLogService.createUserLog(customer.getUser_id(), "User id = "+customer.getUser_id()+" entered invalid otp "+" at "+ new Timestamp((System.currentTimeMillis())));
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Sorry! Your payment was rejected." +
                            " Invalid Otp!");
                }
            }else {
                adminLogService.createUserLog(customer.getUser_id(), "User id = "+customer.getUser_id()+" entered invalid otp "+" at "+ new Timestamp((System.currentTimeMillis())));
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Invalid Otp!");
            }

            return transactionResponse;


        }catch(Exception e){
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your payment was rejected." +
                    " Ran into Exception!");
        }
        return transactionResponse;
    }


    @RequestMapping("/issueCashiersCheck")
    public TransactionResponse issueCashiersCheck(@RequestParam("account_no") int account_no,
                                                  @RequestParam("amount") String amount,
                                                  @RequestParam("beneficiary") String beneficiary_name,
                                                  @RequestParam("customer_id") int customer_id)
            throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try{

            if (!accountCheckService.checkAccountExists(customer_id, account_no)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Invalid payer account chosen!");
                return transactionResponse;
            }

            if (!amountUtils.isValidAmount(amount)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Invalid amount!");
                return transactionResponse;
            }

            Double doubleAmount = Double.parseDouble(amount);
            Double balance = accountBalanceService.getBalance(account_no);

            if (doubleAmount > balance) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Insufficient Balance!");
                return transactionResponse;
            }
            System.out.println("Balance = "+balance);
            boolean status = false;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setFrom_account(account_no);
            //transactionRequest.setTo_account(account_no);
            transactionRequest.setCreated_by(customer_id);
            transactionRequest.setStatus_id(1);
            transactionRequest.setRequest_type(2);
            transactionRequest.setRequest_description("Issue Cashiers Check");
            transactionRequest.setCreated_at(timestamp);
            transactionRequest.setTransaction_amount(doubleAmount);
            transactionRequest.setApproved_by(admin);
            if(doubleAmount >= appConfig.getCriticalAmount()) {
                transactionRequest.setCritical(true);
            } else {
                transactionRequest.setCritical(false);
            }
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
            return transactionResponse;
        }catch(Exception e){
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your payment was rejected." +
                    " Ran into Exceptiom!");
        }
        return transactionResponse;
    }
}
