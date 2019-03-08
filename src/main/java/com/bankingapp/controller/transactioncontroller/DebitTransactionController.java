package com.bankingapp.controller.transactioncontroller;


import com.bankingapp.configuration.AppConfig;
import com.bankingapp.service.accountservice.AccountBalanceService;
import com.bankingapp.service.accountservice.AccountCheckService;
import com.bankingapp.utils.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction")
public class DebitTransactionController {

    @Autowired
    AppConfig appConfig;

    @Autowired
    AccountCheckService accountCheckService;

    @Autowired
    AccountBalanceService accountBalanceService;

    @Autowired
    AmountUtils amountUtils;

    @RequestMapping(value = "/employee/viewtransaction", method = RequestMethod.GET)
    public ModelAndView viewTransactions(@RequestParam("extUserID") String extuserID, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();

        return modelAndView;
    }

    @RequestMapping(value = "/employee/processAccountTransaction", method = RequestMethod.POST)
    public ModelAndView processAccTransactions(@RequestParam("transactionID") String transactionID,
                                               @RequestParam("requestType") String requestType,
                                               @RequestParam("extUserID") String extuserID,
                                               @RequestParam("payeeID") String payeeID,
                                               @RequestParam("amount") String amount,
                                               HttpServletRequest request) {


        ModelAndView modelAndView = new ModelAndView();

        Integer payerId = Integer.parseInt(extuserID);
        Integer payeeId = Integer.parseInt(payeeID);

        if (!accountCheckService.checkAccountExists(payerId)) {
            modelAndView.addObject("success", false);
            modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
                    " Invalid payer account chosen!");
            return modelAndView;
        }

        if (!accountCheckService.checkAccountExists(payeeId)) {
            modelAndView.addObject("success", false);
            modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
                    " Invalid payee account chosen!");
            return modelAndView;
        }

        if (!amountUtils.isValidAmount(amount)) {
            modelAndView.addObject("success", false);
            modelAndView.addObject("error_msg", "Sorry! Your payment was rejected." +
                    " Invalid amount!");
            return modelAndView;
        }

        if (amountUtils.isCritical(amount, appConfig.getCriticalAmount())) {
            modelAndView.addObject("success", false);
            modelAndView.addObject("Sorry! Payment should not " +
                    "exceed $"+appConfig.getCriticalAmount()+"!");
            return modelAndView;
        }

        modelAndView.addObject("extUserID", extuserID);
        modelAndView.addObject("error_msg", "Transaction processed successfully!");
        modelAndView.addObject("success", true);

        return modelAndView;


    }


}
