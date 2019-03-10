package com.bankingapp.controller.accountcontroller;

import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.DebitAccount;
import com.bankingapp.model.transaction.Transaction;
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

    @RequestMapping("/SavingsAccount")
    public ModelAndView SavingAccount(HttpServletRequest request, @RequestParam("id") String id, @RequestParam("interval") String interval)
    {
        try{

            Customer customer = customerService.getCustomer(id);
            DebitAccount savingsAccount = customerAccountService.getSavingsAccount(customer.getId());

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
            model.addObject("customer",customer);
            model.addObject("savingsAccount", savingsAccount );
            model.addObject("TransactionLines",TransactionLines);
            return model;
        }catch(Exception e){
            throw new RuntimeException();
        }

    }
}
