package com.bankingapp.controller.accountcontroller;

import com.bankingapp.configuration.AppConfig;
import com.bankingapp.model.LogParameters;
import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.AccountResponse;
import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.AccountBalanceService;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.service.accountservice.AccountUpdateService;
import com.bankingapp.service.adminlogservice.AdminLogService;
import com.bankingapp.service.customerservice.CustomerAccountService;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.service.loginservice.SessionService;
import com.bankingapp.service.transactionservice.TransactionServiceImpl;
import com.bankingapp.utils.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerAccountController {

    @Autowired
    AppConfig appConfig;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerAccountService customerAccountService;

    @Autowired
    AccountCheckService accountCheckService;

    @Autowired
    AmountUtils amountUtils;

    @Autowired
    AccountBalanceService accountBalanceService;

    @Autowired
    AccountUpdateService accountUpdateService;

    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    AdminLogService adminLogService;

    @Autowired
    LogParameters logParameters;

    @Autowired
    SessionService sessionService;

    @RequestMapping("/getAccounts")
    public List<AccountResponse> getAccounts(@RequestParam("customerId") int customerId, HttpServletRequest req) throws Exception
    {

        List<AccountResponse> accountResponseList = new ArrayList<>();
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
//
//        try {
//            Cookie cookie = WebUtils.getCookie(req, "heroku-nav-data");
//            System.out.println(cookie);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
        try {
            for (Cookie c : req.getCookies()) {
                if (c.getName().equals("website"))
                    System.out.println(c.getValue());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        try{

            List<Account> savingsAccounts = customerAccountService.getAccounts(customerId);

            for(Account account : savingsAccounts) {

                List<Transaction> transactionList = transactionService.getAllTransactions(account.getAccount_no());
                AccountResponse accountResponse = new AccountResponse();
                if(account.getAccount_type() == 1) {
                    accountResponse.setAccountType("Savings");
                } else {
                    accountResponse.setAccountType("Checking");
                }

                accountResponse.setCustomerId(account.getUser_id());
                accountResponse.setAccount(account);
                accountResponse.setTransactionList(transactionList);

                accountResponseList.add(accountResponse);
            }

        }catch(Exception e){
            throw new RuntimeException();
        }

        adminLogService.createUserLog(customerId, logParameters.GET_ACCOUNTS);
        return accountResponseList;

    }

    @RequestMapping("/SavingsAccounts")
    public List<AccountResponse> SavingAccount(@RequestParam("customerId") int customerId)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        try{

            List<Account> savingsAccounts = customerAccountService.getAccounts(customerId, 1);

            for(Account account : savingsAccounts) {
                AccountResponse accountResponse = new AccountResponse();
                accountResponse.setAccountType("Savings");
                accountResponse.setCustomerId(account.getUser_id());
                accountResponse.setAccount(account);
                accountResponseList.add(accountResponse);
            }

        }catch(Exception e){
            throw new RuntimeException();
        }

        return accountResponseList;

    }

    @RequestMapping("/CheckingAccounts")
    public List<AccountResponse> CheckingAccount(@RequestParam("customerId") int customerId)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        try{

            List<Account> checkingAccountsList = customerAccountService.getAccounts(customerId, 2);

            for(Account account : checkingAccountsList) {
                AccountResponse accountResponse = new AccountResponse();
                accountResponse.setAccountType("Checking");
                accountResponse.setCustomerId(account.getUser_id());
                accountResponse.setAccount(account);
                accountResponseList.add(accountResponse);
            }

        }catch(Exception e){
            throw new RuntimeException();
        }

        return accountResponseList;

    }

    @RequestMapping("/CheckingAccount")
    public ModelAndView CheckingAccount(@RequestParam("id") int customerId, @RequestParam("interval") String interval)
    {
        try{
            Account savingsAccount = customerAccountService.getCheckingAccount(customerId);

            List<Transaction> TransactionLines = new ArrayList<Transaction>();
            if (interval.equals("Last month"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccount_no(), 1);
            }else if (interval.equals("Last 3 months"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccount_no(), 3);
            }else if (interval.equals("Last 6 months"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccount_no(), 6);
            }else
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccount_no(), 1);
            }
            ModelAndView model = new ModelAndView("");
            model.addObject("customer",customerId);
            model.addObject("savingsAccount", savingsAccount );
            model.addObject("TransactionLines",TransactionLines);
            return model;
        }catch(Exception e){
            throw new RuntimeException();
        }

    }

    @RequestMapping("/WithdrawMoney")
    public TransactionResponse withdrawMoneyToSavingsAccount(@RequestParam("account_no") int account_no, @RequestParam("amount") String amount)
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


//    @RequestMapping("/WithdrawMoney")
//    public ModelAndView WithdrawMoneyFromSavingsAccount(@RequestParam("id") int customerId, @RequestParam("amount") String amount)
//    {
//        try{
//
//            Account savingsAccount = customerAccountService.getCheckingAccount(customerId);
//
//            ModelAndView modelAndView = new ModelAndView("");
//
//            int payerId = savingsAccount.getAccount_no();
//            if (!accountCheckService.checkAccountExists(payerId)) {
//                modelAndView.addObject("success", false);
//                modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
//                        " Invalid payer account chosen!");
//                return modelAndView;
//            }
//
//            Double doubleAmount = amountUtils.convertToDouble(amount);
//            if(!accountBalanceService.validateDebitAmount(payerId, doubleAmount)) {
//                modelAndView.addObject("success", false);
//                modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
//                        " Insufficinet Balance!");
//                return modelAndView;
//            }
//
//
//            ModelAndView model = new ModelAndView("");
//            model.addObject("customer",customerId);
//            model.addObject("savingsAccount", savingsAccount );
//            return model;
//        }catch(Exception e){
//            throw new RuntimeException();
//        }

//    }
}