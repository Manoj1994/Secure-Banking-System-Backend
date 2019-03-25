package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.model.request.Request;
import com.bankingapp.service.requestservice.RequestService;
import com.bankingapp.service.transactionservice.TransactionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    TransactionRequestService transactionRequestService;

    @RequestMapping(value = "/addRequest", method = RequestMethod.GET)
    public TransactionResponse addNewRequest(@RequestParam("requester_id") int requester_id, @RequestParam("request_type") String request_type,@RequestParam("requested_value") String requested_value,@RequestParam("description") String description)
    {
        TransactionResponse transactionResponse = new TransactionResponse();

        try{
            RequestService service=new RequestService();
            Request request=new Request();
            request.setRequesterId(requester_id);
            request.setRequest_type(request_type);
            request.setRequested_value(requested_value);
            request.setDescription(description);

            if (!service.add_new_request(request)) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your request is not valid!");
                return transactionResponse;
            }
            else{
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("The request was successfully added to list of Pending requests");
            }
        }catch(Exception e){

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has." + " ran into Exception!");
        }
        return transactionResponse;
    }
}
