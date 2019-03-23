package com.bankingapp.controller.employeecontroller;

import com.bankingapp.model.employee.Employee;
import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.service.transactionservice.TransactionServiceImpl;
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
    public Boolean approveTransaction(int request_id) {
        try{

            boolean status = transactionService.approveTransaction(request_id);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            TransactionRequest transactionRequest =

        }catch(Exception e){
        }

        return false;
    }
}
