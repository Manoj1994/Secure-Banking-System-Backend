package com.bankingapp.controller.accountcontroller;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.service.accountservice.AccountBalanceService;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.service.accountservice.AccountUpdateService;
import com.bankingapp.service.customerservice.CustomerAccountService;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.utils.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerAccountController {

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

    @RequestMapping("/SavingsAccount")
    public ModelAndView SavingAccount(HttpServletRequest request, @RequestParam("id") String customerId, @RequestParam("interval") String interval)
    {
        try{

            Account savingsAccount = customerAccountService.getSavingsAccount(customerId);

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

    @RequestMapping("/CheckingAccount")
    public ModelAndView CheckingAccount(HttpServletRequest request, @RequestParam("id") String customerId, @RequestParam("interval") String interval)
    {
        try{
            Account savingsAccount = customerAccountService.getSavingsAccount(customerId);

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

    @RequestMapping("/DepositMoney")
    public ModelAndView DepositMoneyToSavingsAccount(HttpServletRequest request, @RequestParam("id") String customerId, @RequestParam("amount") String amount)
    {
        try{

            Account savingsAccount = customerAccountService.getSavingsAccount(customerId);

            ModelAndView modelAndView = new ModelAndView("");

            int payerId = savingsAccount.getAccount_no();
            if (!accountCheckService.checkAccountExists(payerId)) {
                modelAndView.addObject("success", false);
                modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
                        " Invalid payer account chosen!");
                return modelAndView;
            }

            if (!amountUtils.isValidAmount(amount)) {
                modelAndView.addObject("success", false);
                modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
                        " Invalid amount!");
                return modelAndView;
            }


            ModelAndView model = new ModelAndView("");
            model.addObject("customer",customerId);
            model.addObject("savingsAccount", savingsAccount );
            return model;
        }catch(Exception e){
            throw new RuntimeException();
        }

    }

    @RequestMapping("/WithdrawMoney")
    public ModelAndView WithdrawMoneyFromSavingsAccount(HttpServletRequest request, @RequestParam("id") String customerId, @RequestParam("amount") String amount)
    {
        try{

            Account savingsAccount = customerAccountService.getSavingsAccount(customerId);

            ModelAndView modelAndView = new ModelAndView("");

            int payerId = savingsAccount.getAccount_no();
            if (!accountCheckService.checkAccountExists(payerId)) {
                modelAndView.addObject("success", false);
                modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
                        " Invalid payer account chosen!");
                return modelAndView;
            }

            Double doubleAmount = amountUtils.convertToDouble(amount);
            if(!accountBalanceService.validateDebitAmount(payerId, doubleAmount)) {
                modelAndView.addObject("success", false);
                modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
                        " Insufficinet Balance!");
                return modelAndView;
            }


            ModelAndView model = new ModelAndView("");
            model.addObject("customer",customerId);
            model.addObject("savingsAccount", savingsAccount );
            return model;
        }catch(Exception e){
            throw new RuntimeException();
        }

    }
}