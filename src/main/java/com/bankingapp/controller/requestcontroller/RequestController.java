package com.bankingapp.controller.requestcontroller;

import java.util.List;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.model.request.Request;
import com.bankingapp.service.requestservice.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    RequestService requestService;

    @RequestMapping(value = "/addRequest", method = RequestMethod.GET)
    public TransactionResponse addNewRequest(@RequestParam("requester_id") int requester_id, @RequestParam("request_type") String request_type,@RequestParam("requested_value") String requested_value,@RequestParam("description") String description)
    {
        TransactionResponse transactionResponse = new TransactionResponse();

        try{
            Request request=new Request();
            request.setRequesterId(requester_id);
            request.setRequest_type(request_type);
            request.setRequested_value(requested_value);
            request.setDescription(description);

            if (!requestService.add_new_request(request)) {
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

    @RequestMapping(value = "/getAllRequest", method = RequestMethod.GET)
    public List<Request> getAllRequest()
    {

        List<Request> requests=null;
        try{
            requests= requestService.getByAllRequest();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            return requests;
        }
    }

    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    public TransactionResponse approveRequest(@RequestParam("id") int id,@RequestParam("approver_id") int approver_id)
    {
        TransactionResponse transactionResponse = new TransactionResponse();

        boolean status=true;
        try{
            status=requestService.approveRequest(id,approver_id);
            transactionResponse.setMessage("Request approved");
            transactionResponse.setSuccess(true);
        }
        catch(Exception e){
            e.printStackTrace();
            status=false;
            transactionResponse.setMessage("Request approval failed due ti unexcepted error.Please Check if your request was valid");
            transactionResponse.setSuccess(false);
        }
        finally {
            return transactionResponse;
        }
    }

    @RequestMapping(value = "/reject", method = RequestMethod.GET)
    public TransactionResponse rejectRequest(@RequestParam("id") int id,@RequestParam("approver_id") int approver_id)
    {
        TransactionResponse transactionResponse = new TransactionResponse();
        boolean status=true;

        try{
            status=requestService.rejectRequest(id,approver_id);
            transactionResponse.setMessage("Request approved");
            transactionResponse.setSuccess(true);
        }
        catch(Exception e){
            e.printStackTrace();
            status=false;
            transactionResponse.setMessage("Request approval failed due ti unexcepted error.Please Check if your request was valid");
            transactionResponse.setSuccess(false);
        }
        finally {
            return transactionResponse;
        }
    }

}
