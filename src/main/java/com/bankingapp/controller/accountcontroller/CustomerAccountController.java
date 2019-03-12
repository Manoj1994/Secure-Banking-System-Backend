package com.bankingapp.controller.accountcontroller;

import com.bankingapp.model.account.SavingsAccount;
import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.service.customerservice.CustomerAccountService;
import com.bankingapp.service.customerservice.CustomerService;
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

    @RequestMapping("/SavingsAccount")
    public ModelAndView SavingAccount(HttpServletRequest request, @RequestParam("id") String customerId, @RequestParam("interval") String interval)
    {
        try{

            SavingsAccount savingsAccount = customerAccountService.getSavingsAccount(customerId);

            List<Transaction> TransactionLines = new ArrayList<Transaction>();
            if (interval.equals("Last month"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccountNumber(), 1);
            }else if (interval.equals("Last 3 months"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccountNumber(), 3);
            }else if (interval.equals("Last 6 months"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccountNumber(), 6);
            }else
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccountNumber(), 1);
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
            SavingsAccount savingsAccount = customerAccountService.getSavingsAccount(customerId);

            List<Transaction> TransactionLines = new ArrayList<Transaction>();
            if (interval.equals("Last month"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccountNumber(), 1);
            }else if (interval.equals("Last 3 months"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccountNumber(), 3);
            }else if (interval.equals("Last 6 months"))
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccountNumber(), 6);
            }else
            {
                TransactionLines = customerAccountService.getTransactions(savingsAccount.getAccountNumber(), 1);
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
    public ModelAndView DepositMoneyToSavingsAccount(HttpServletRequest request, @RequestParam("id") String customerId, @RequestParam("interval") Double amount)
    {
        try{

            SavingsAccount savingsAccount = customerAccountService.getSavingsAccount(customerId);

            ModelAndView modelAndView = new ModelAndView("");

            int payerId = savingsAccount.getAccountNumber();
            if (!accountCheckService.checkAccountExists(payerId)) {
                modelAndView.addObject("success", false);
                modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
                        " Invalid payer account chosen!");
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
