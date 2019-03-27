package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.request.Request;
import com.bankingapp.service.requestservice.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/request/employee")
public class EmployeeRequestController {

    @Autowired
    RequestService requestService;

    @RequestMapping(value = "/getRequests", method = RequestMethod.GET)
    public List<Request> gerRequests() {

        List<Request> requests = new ArrayList<>();
        try {
            requests = requestService.getByAllRequest();

            System.out.println(requests);
            return requests;

        } catch(Exception e) {

        }
        return requests;
    }
}