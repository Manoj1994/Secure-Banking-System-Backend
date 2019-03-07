package com.bankingapp.controller.transactioncontroller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction")
public class EmployeeTransactionController {


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

        Integer payerID = Integer.parseInt(extuserID);
        ModelAndView modelAndView = new ModelAndView();

        return modelAndView;


    }


}
