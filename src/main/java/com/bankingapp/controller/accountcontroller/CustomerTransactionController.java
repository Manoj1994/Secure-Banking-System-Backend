package com.bankingapp.controller.accountcontroller;

import com.bankingapp.configuration.AppConfig;
import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.AccountBalanceService;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.service.accountservice.AccountUpdateService;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.transactionservice.TransactionRequestService;
import com.bankingapp.utils.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

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
    EmployeeService employeeService;

    @Autowired
    AccountUpdateService accountUpdateService;

    private int admin = 3;

    @RequestMapping("/TransferMoneyFromAccount")
    public TransactionResponse transferMoneyToSavingsAccount(@RequestParam("from_account_no") int from_account_no, @RequestParam("to_account_no") int to_account_no, @RequestParam("amount") String amount, @RequestParam("routing_no") int routing_no)
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

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            TransactionRequest transactionRequest = new TransactionRequest();

            transactionRequest.setFrom_account(from_account_no);
            transactionRequest.setTo_account(to_account_no);

            transactionRequest.setCreated_by(from_account_no);
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

    @RequestMapping("/TransferMoneyFromAccountViaEmail")
    public TransactionResponse transferMoneyToSavingsAccountViaEmail(@RequestParam("from_account_no") int from_account_no, @RequestParam("email") int to_account_no, @RequestParam("amount") String amount)
    {
        TransactionResponse transactionResponse = new TransactionResponse();
        try{

            if (!accountCheckService.checkAccountExists(from_account_no)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your transaction request was rejected." +
                        " Invalid payer account chosen!");
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

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            TransactionRequest transactionRequest = new TransactionRequest();

            transactionRequest.setFrom_account(from_account_no);
            transactionRequest.setTo_account(to_account_no);

            transactionRequest.setCreated_by(from_account_no);
            transactionRequest.setStatus_id(1);
            transactionRequest.setCreated_at(timestamp);
            transactionRequest.setTransaction_amount(doubleAmount);

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
        }catch(Exception e){

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your payment was rejected." +
                    " Ran into Exceptiom!");
        }

        return transactionResponse;

    }


    @RequestMapping("/issueCashiersCheck")
    public TransactionResponse issueCashiersCheck(@RequestParam("account_no") int account_no, @RequestParam("amount") String amount, @RequestParam("beneficiary") String beneficiary_name)
    {
        TransactionResponse transactionResponse = new TransactionResponse();
        try{

            if (!accountCheckService.checkAccountExists(account_no)) {

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

            if(doubleAmount < appConfig.getCriticalAmount()) {
                status = accountUpdateService.updateBalance(account_no, balance - doubleAmount);
            }

            if(!status) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Internal Server Error!");
                return transactionResponse;
            } else {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Money Withdrawn, Your transaction is successful");
            }



        }catch(Exception e){

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your payment was rejected." +
                    " Ran into Exceptiom!");
        }

        return transactionResponse;

    }
}
