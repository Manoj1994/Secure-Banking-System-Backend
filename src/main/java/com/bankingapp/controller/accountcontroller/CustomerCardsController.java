package com.bankingapp.controller.accountcontroller;

import com.bankingapp.model.Parameters;
import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.CreditCard;
import com.bankingapp.model.account.DebitCard;
import com.bankingapp.service.accountservice.CreditCardService;
import com.bankingapp.service.accountservice.DebitCardService;
import com.bankingapp.service.adminlogservice.AdminLogService;
import com.bankingapp.service.customerservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerCardsController {

    @Autowired
    DebitCardService debitCardService;

    @Autowired
    CreditCardService creditCardService;

    @Autowired
    CustomerService customerService;

    @Autowired
    AdminLogService adminLogService;

    @Autowired
    Parameters parameters;

    @RequestMapping("/getDebitCards")
    public List<DebitCard> getDebitcards(@RequestParam("customerId") int customerId) {

        List<DebitCard> allDebitCards = new ArrayList<>();
        try {
            List<Account> accounts = customerService.getAllAccounts(customerId);
            System.out.println(accounts);
            for (Account account : accounts) {
                List<DebitCard> debitCards = debitCardService.getDebitCards(account.getAccount_no());
                allDebitCards.addAll(debitCards);
            }
            return allDebitCards;
        } catch(Exception e) {

        }

        adminLogService.createUserLog(customerId, parameters.GET_DEBIT_CARDS);
        return allDebitCards;
    }

    @RequestMapping("/newDebitCard")
    public Boolean addDebitCard(@RequestParam("account_no") int account_no) {
        try {

            DebitCard debitCard = new DebitCard();
            debitCard.setAccount_no(account_no);
            debitCardService.addNewDebitCard(debitCard);

            return true;
        } catch (Exception e) {

        }
        return false;
    }

    @RequestMapping("/getCreditCards")
    public List<CreditCard> getCreditcards(@RequestParam("customerId") int customerId) {

        List<CreditCard> allCreditCards = new ArrayList<>();
        try {
            List<Account> accounts = customerService.getAllAccounts(customerId);
            System.out.println(accounts);
            for (Account account : accounts) {
                List<CreditCard> creditCards = creditCardService.getCreditCards(account.getAccount_no());
                allCreditCards.addAll(creditCards);
            }
            return allCreditCards;
        } catch(Exception e) {

        }

        adminLogService.createUserLog(customerId, parameters.GET_CREDIT_CARDS);
        return allCreditCards;
    }

    @RequestMapping("/newCreditCard")
    public Boolean addCreditCard(@RequestParam("account_no") int account_no, @RequestParam("limit") int balance) {
        try {

            DebitCard debitCard = new DebitCard();
            debitCard.setAccount_no(account_no);
            debitCardService.addNewDebitCard(debitCard);

            //adminLogService.createUserLog(account_no, parameters.)
            return true;
        } catch (Exception e) {

        }
        return false;
    }
}