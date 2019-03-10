package com.bankingapp.controller.transactioncontroller;

import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.service.transactionservice.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionService;

    @RequestMapping("/alltransactions")
    public ModelAndView TransactionContoller() {
        try {

            List<Transaction> complete_list = transactionService.getAllCompleted();
            List<Transaction> pending_list = transactionService.getAllPending();
            ModelAndView model = new ModelAndView("employeePages/employeeTransaction");
            model.addObject("complete_list", complete_list);
            model.addObject("pending_list", pending_list);
            return model;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @RequestMapping(value = "/transactionsearch", method = RequestMethod.POST)
    public ModelAndView TransactionSearch(@RequestParam("transactionID") String transactionID) {

        ModelAndView model = new ModelAndView("employeePages/employeeTransaction");
        try {

            List<Transaction> transactionsList = null;
            transactionsList = transactionService.getById(Integer.parseInt(transactionID));
            model.addObject("complete_list", transactionsList);
            return model;
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    @RequestMapping(value = "/approvetransaction", method = RequestMethod.POST)
    public ModelAndView TransactionApproveContoller(@RequestParam("transactionID") String transactionID) {

        ModelAndView model = new ModelAndView("redirect:" + "/employee/transaction");
        try {
            boolean approved = transactionService.approveTransaction(Integer.parseInt(transactionID));
            model.addObject("error_msg", "Transaction Approved!");
            return model;
        } catch (Exception e) {
            model.addObject("error_msg", "Couldn't process the transaction!");
        }
        return model;

    }

    @RequestMapping(value = "/transactionreject", method = RequestMethod.POST)
    public ModelAndView TransactionRejectContoller(@RequestParam("transactionID") String transactionID) {

        ModelAndView model = new ModelAndView("redirect:" + "/employee/transaction");
        try {
            boolean rejected = transactionService.rejectTransaction(Integer.parseInt(transactionID));
            model.addObject("error_msg", "Transaction Rejected!");
            return model;
        } catch (Exception e) {
            model.addObject("error_msg", "Couldn't process the transaction!");
        }
        return model;
    }
}
