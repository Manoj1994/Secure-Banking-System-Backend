package com.bankingapp.controller.employeecontroller;

import com.bankingapp.configuration.AppConfig;
import com.bankingapp.model.employee.Employee;
import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.model.transaction.TransactionParameters;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.AccountBalanceService;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.service.accountservice.AccountUpdateService;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.loginservice.SessionService;
import com.bankingapp.service.transactionservice.TransactionRequestService;
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

    @Autowired
    TransactionRequestService transactionRequestService;

    @Autowired
    TransactionParameters transactionParameters;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    SessionService sessionService;

    private static final int admin = 3;

    @RequestMapping("/viewTransactions")
    public List<TransactionRequest> viewPendingTransactions(@RequestParam("employee_id") int employee_id) throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        List<TransactionRequest> transactionRequestList = new ArrayList<>();
        try{

            System.out.println(employee_id);
            int role_id = employeeService.getRoleOfEmployee(employee_id);
            System.out.println(role_id);

            if(role_id == 1) {
                transactionRequestList = transactionService.getAllPending(false);
            } else {
                transactionRequestList = transactionService.getAllPending(true);
            }

            return transactionRequestList;

        }catch(Exception e){
        }

        return transactionRequestList;
    }

    @RequestMapping("/handleTransaction")
    public TransactionResponse getRequests(@RequestParam("request_id") int request_id,
                                           @RequestParam("employee_id") int employee_id,
                                           @RequestParam("action") int action) throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try {

            TransactionRequest transactionRequest = transactionService.getPendingTransaction(request_id);
            Double transactionAmount = transactionRequest.getTransaction_amount();

            if (action == 0) {

                transactionRequest.setStatus_id(3);
                transactionRequest.setApproved_by(employee_id);

                transactionRequestService.saveTransactionRequest(transactionRequest);

                transactionResponse.setMessage("Transaction is Rejected");
                transactionResponse.setSuccess(true);
                return transactionResponse;
            } else if(action == 1) {

                if (transactionRequest.getRequest_type() == transactionParameters.DEPOSIT_MONEY) {

                    int accountNumber = transactionRequest.getTo_account();
                    if (!accountCheckService.checkAccountExists(accountNumber)) {

                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Invalid account chosen!");
                        return transactionResponse;
                    }

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    boolean status = transactionService.approveTransaction(request_id);

                    if (!status) {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Internal Server Error!");
                        return transactionResponse;
                    } else {

                        Transaction transaction1 = new Transaction();

                        transaction1.setAccount_no(accountNumber);
                        transaction1.setBalance(accountBalanceService.getBalance(accountNumber));
                        transaction1.setRequest_id(transactionRequest.getRequest_id());

                        String debit_description = "Funds added to your account " + accountNumber;
                        transaction1.setDescription(debit_description);

                        transaction1.setStatus(3);
                        transaction1.setTransaction_type(1);
                        transaction1.setTransaction_timestamp(timestamp);
                        transaction1.setTransaction_amount(transactionAmount);

                        transactionService.save(transaction1);

                        accountUpdateService.updateBalance(accountNumber, transactionAmount);

                        transactionRequest.setStatus_id(2);
                        transactionRequest.setApproved_by(employee_id);

                        transactionRequestService.saveTransactionRequest(transactionRequest);

                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Your transaction is Successful");

                        return transactionResponse;


                    }
                } else if (transactionRequest.getRequest_type() == transactionParameters.WITHDRAW_MONEY) {

                    int accountNumber = transactionRequest.getFrom_account();
                    if (!accountCheckService.checkAccountExists(accountNumber)) {

                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Invalid account chosen!");
                        return transactionResponse;
                    }

                    Double balance = accountBalanceService.getBalance(accountNumber);

                    if (transactionAmount > balance) {

                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Insufficient Balance!");
                        return transactionResponse;
                    }

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    boolean status = transactionService.approveTransaction(request_id);

                    if (!status) {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Internal Server Error!");
                        return transactionResponse;
                    } else {

                        Transaction transaction1 = new Transaction();

                        transaction1.setAccount_no(accountNumber);
                        transaction1.setBalance(accountBalanceService.getBalance(accountNumber));
                        transaction1.setRequest_id(transactionRequest.getRequest_id());

                        String debit_description = "Money deleted from your account " + accountNumber;
                        transaction1.setDescription(debit_description);

                        transaction1.setStatus(3);
                        transaction1.setTransaction_type(1);
                        transaction1.setTransaction_timestamp(timestamp);
                        transaction1.setTransaction_amount(transactionAmount);

                        transactionService.save(transaction1);

                        accountUpdateService.updateBalance(accountNumber, -transactionAmount);

                        transactionRequest.setStatus_id(2);
                        transactionRequest.setApproved_by(employee_id);

                        transactionRequestService.saveTransactionRequest(transactionRequest);

                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Your transaction is Successful");

                        return transactionResponse;


                    }
                } else if (transactionRequest.getRequest_type() == transactionParameters.ISSUE_CASHIERS_CHECK) {

                    int accountNumber = transactionRequest.getFrom_account();
                    if (!accountCheckService.checkAccountExists(accountNumber)) {

                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Invalid account chosen!");
                        return transactionResponse;
                    }

                    Double balance = accountBalanceService.getBalance(accountNumber);

                    if (transactionAmount > balance) {

                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Insufficient Balance!");
                        return transactionResponse;
                    }

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    boolean status = transactionService.approveTransaction(request_id);

                    if (!status) {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Internal Server Error!");
                        return transactionResponse;
                    } else {

                        Transaction transaction1 = new Transaction();

                        transaction1.setAccount_no(accountNumber);
                        transaction1.setBalance(accountBalanceService.getBalance(accountNumber));
                        transaction1.setRequest_id(transactionRequest.getRequest_id());

                        String debit_description = "Cashiers check issued from your account " + accountNumber;
                        transaction1.setDescription(debit_description);

                        transaction1.setStatus(3);
                        transaction1.setTransaction_type(1);
                        transaction1.setTransaction_timestamp(timestamp);
                        transaction1.setTransaction_amount(transactionAmount);

                        transactionService.save(transaction1);

                        accountUpdateService.updateBalance(accountNumber, -transactionAmount);

                        transactionRequest.setStatus_id(2);
                        transactionRequest.setApproved_by(employee_id);

                        transactionRequestService.saveTransactionRequest(transactionRequest);

                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Your transaction is Successful");

                        return transactionResponse;


                    }
                } else if(transactionRequest.getRequest_type() == transactionParameters.TRANSFER){

                    int payerAccountNumber = transactionRequest.getFrom_account();
                    int payeeAccountNumber = transactionRequest.getTo_account();

                    Double balance = accountBalanceService.getBalance(payerAccountNumber);

                    if (!accountCheckService.checkAccountExists(payerAccountNumber)) {

                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
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

                    if (!status) {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Sorry! transaction was rejected." +
                                " Internal Server Error!");
                        return transactionResponse;
                    } else {

                        //DebitTransaction

                        if (payeeAccountNumber == payerAccountNumber) {

                            Transaction transaction1 = new Transaction();

                            transaction1.setAccount_no(payerAccountNumber);
                            transaction1.setBalance(accountBalanceService.getBalance(payerAccountNumber));
                            transaction1.setRequest_id(transactionRequest.getRequest_id());

                            String debit_description = "Debited from your account to " + payeeAccountNumber;
                            transaction1.setDescription(debit_description);

                            transaction1.setStatus(3);
                            transaction1.setTransaction_type(1);
                            transaction1.setTransaction_timestamp(timestamp);
                            transaction1.setTransaction_amount(transactionAmount);

                            transactionService.save(transaction1);

                            accountUpdateService.updateBalance(payerAccountNumber, -transactionAmount);

                            transactionRequest.setStatus_id(2);
                            transactionRequest.setApproved_by(employee_id);

                            transactionRequestService.saveTransactionRequest(transactionRequest);

                            transactionResponse.setSuccess(true);
                            transactionResponse.setMessage("Your transaction is Successful");

                            return transactionResponse;


                        } else {

                            Transaction transaction1 = new Transaction();

                            transaction1.setAccount_no(payerAccountNumber);
                            transaction1.setBalance(accountBalanceService.getBalance(payerAccountNumber));
                            transaction1.setRequest_id(transactionRequest.getRequest_id());

                            String debit_description = "Debited from your account to " + payeeAccountNumber;
                            transaction1.setDescription(debit_description);

                            transaction1.setStatus(1);
                            transaction1.setTransaction_type(1);
                            transaction1.setTransaction_timestamp(timestamp);
                            transaction1.setTransaction_amount(transactionAmount);

                            transactionService.save(transaction1);

                            accountUpdateService.updateBalance(payerAccountNumber, -transactionAmount);


                            // Credit Transaction

                            Transaction transaction2 = new Transaction();

                            transaction2.setAccount_no(payeeAccountNumber);
                            transaction2.setBalance(accountBalanceService.getBalance(payeeAccountNumber));
                            transaction2.setRequest_id(transactionRequest.getRequest_id());

                            String credit_description = "Credited from  account " + payeeAccountNumber + " To your account";
                            transaction2.setDescription(credit_description);

                            transaction2.setStatus(1);
                            transaction2.setTransaction_type(2);
                            transaction2.setTransaction_timestamp(timestamp);
                            transaction2.setTransaction_amount(transactionAmount);

                            transactionService.save(transaction2);
                            accountUpdateService.updateBalance(payeeAccountNumber, transactionAmount);

                            transactionRequest.setStatus_id(2);
                            transactionRequest.setApproved_by(employee_id);

                            transactionRequestService.saveTransactionRequest(transactionRequest);

                            transactionResponse.setSuccess(true);
                            transactionResponse.setMessage("Your transaction is Successful");

                            return transactionResponse;
                        }
                    }
                } else {
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Invalid Transaction");
                }
            } else {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("No action selected");
            }

            return transactionResponse;

        }catch(Exception e){
        }

        transactionResponse.setSuccess(false);
        transactionResponse.setMessage("Error while processing transaction");
        return transactionResponse;
    }

    @RequestMapping("/DepositMoney")
    public TransactionResponse DepositMoneyToSavingsAccount(@RequestParam("account_no") int account_no,
                                                            @RequestParam("amount") String amount,
                                                            @RequestParam("employee_id") int employee_id)
            throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
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

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            TransactionRequest transactionRequest = new TransactionRequest();

            //transactionRequest.setFrom_account(account_no);
            transactionRequest.setTo_account(account_no);

            transactionRequest.setCreated_by(employee_id);
            transactionRequest.setStatus_id(1);
            transactionRequest.setRequest_type(3);
            transactionRequest.setRequest_description("Add Funds");
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

    @RequestMapping("/WithDrawMoney")
    public TransactionResponse WithDrawtMoneyFromSavingsAccount(@RequestParam("account_no") int account_no,
                                                            @RequestParam("amount") String amount,
                                                            @RequestParam("employee_id") int employee_id)
            throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try{

            if (!accountCheckService.checkAccountExists(account_no)) {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your payment was rejected." +
                        " Invalid account chosen!");
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

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            TransactionRequest transactionRequest = new TransactionRequest();

            //transactionRequest.setFrom_account(account_no);
            transactionRequest.setTo_account(account_no);

            transactionRequest.setCreated_by(employee_id);
            transactionRequest.setStatus_id(1);
            transactionRequest.setRequest_type(4);
            transactionRequest.setRequest_description("Withdraw Funds");
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
