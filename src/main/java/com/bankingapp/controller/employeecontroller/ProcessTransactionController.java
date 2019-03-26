package com.bankingapp.controller.employeecontroller;

import com.bankingapp.configuration.AppConfig;
import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.AccountBalanceService;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.service.accountservice.AccountUpdateService;
import com.bankingapp.service.transactionservice.TransactionServiceImpl;
import com.bankingapp.utils.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class ProcessTransactionController {


    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    AccountUpdateService accountUpdateService;

    @Autowired
    AccountBalanceService accountBalanceService;

    @Autowired
    AccountCheckService accountCheckService;

    @Autowired
    AppConfig appConfig;

    @Autowired
    AmountUtils amountUtils;

    @RequestMapping("/ViewPendingTransactions")
    public List<TransactionRequest> viewPendingTransactions(@RequestParam("employee_id") int employee_id) {

        List<TransactionRequest> transactionRequestList = new ArrayList<>();
        try{

            transactionRequestList = transactionService.getAllPending(employee_id);

            return transactionRequestList;

        }catch(Exception e){
        }

        return transactionRequestList;
    }

    @RequestMapping("/approvedTransaction")
    public TransactionResponse approveTransaction(@RequestParam("request_id")  int request_id) {

        TransactionResponse transactionResponse = new TransactionResponse();
        try{


            TransactionRequest transactionRequest = transactionService.getPendingTransaction(request_id);

            int payerAccountNumber = transactionRequest.getFrom_account();
            int payeeAccountNumber = transactionRequest.getTo_account();
            Double transactionAmount = transactionRequest.getTransaction_amount();

            Double balance = accountBalanceService.getBalance(payerAccountNumber);



            if (!accountCheckService.checkAccountExists(payerAccountNumber)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! transaction request was rejected." +
                        " Invalid payer account chosen!");
                return transactionResponse;
            }

            if (!accountCheckService.checkAccountExists(payeeAccountNumber)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! transaction was rejected." +
                        " Invalid payee account chosen!");
                return transactionResponse;
            }

            if (transactionAmount > balance) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! transaction was rejected." +
                        " Insufficient Balance!");
                return transactionResponse;
            }

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            boolean status = transactionService.approveTransaction(request_id);

            if(!status) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! transaction request was rejected." +
                        " Internal Server Error!");
                return transactionResponse;
            } else {

                //DebitTransaction

                Transaction transaction1 = new Transaction();

                transaction1.setAccount_no(payerAccountNumber);
                transaction1.setBalance(accountBalanceService.getBalance(payerAccountNumber));
                transaction1.setRequest_id(transactionRequest.getRequest_id());

                String debit_description = "Debited from your account to "+payeeAccountNumber;
                transaction1.setDescription(debit_description);

                transaction1.setStatus(1);
                transaction1.setTransaction_type(1);
                transaction1.setTransaction_timestamp(timestamp);
                transaction1.setTransaction_amount(transactionAmount);

                transactionService.save(transaction1);

                accountUpdateService.updateMoney(payerAccountNumber, -transactionAmount);


                // Credit Transaction

                Transaction transaction2 = new Transaction();

                transaction2.setAccount_no(payerAccountNumber);
                transaction2.setBalance(accountBalanceService.getBalance(payerAccountNumber));
                transaction2.setRequest_id(transactionRequest.getRequest_id());

                String credit_description = "Credited from  account "+payeeAccountNumber+" To your account";
                transaction2.setDescription(credit_description);

                transaction2.setStatus(1);
                transaction2.setTransaction_type(2);
                transaction2.setTransaction_timestamp(timestamp);
                transaction2.setTransaction_amount(transactionAmount);


                transactionService.save(transaction2);
                accountUpdateService.updateMoney(payeeAccountNumber, transactionAmount);

                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Your transaction request is Pending");

                return transactionResponse;
            }


        }catch(Exception e){
        }

        transactionResponse.setSuccess(false);
        transactionResponse.setMessage("Error while processing transaction");
        return transactionResponse;
    }

    @RequestMapping("/DepositMoney")
    public TransactionResponse DepositMoneyToSavingsAccount(@RequestParam("account_no") int account_no, @RequestParam("amount") String amount)
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
            boolean status = false;

            if(doubleAmount < appConfig.getCriticalAmount()) {
                status = accountUpdateService.updateBalance(account_no, doubleAmount);
            }

            if(!status) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Internal Server Error!");
                return transactionResponse;
            } else {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Money Deposited, Your transaction is successful");
            }
        }catch(Exception e){
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your payment was rejected." +
                    " Ran into Exceptiom!");
        }
        return transactionResponse;
    }
}