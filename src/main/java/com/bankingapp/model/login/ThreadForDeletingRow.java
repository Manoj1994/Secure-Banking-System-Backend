package com.bankingapp.model.login;

import com.bankingapp.service.loginservice.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadForDeletingRow extends Thread{

    @Autowired
    SessionService sessionService;

    @Override
    public void run() {
        System.out.println(getName() + " is running");
        try {
            //sessionService.deleteInvalidRows();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getName() + " is running");
    }

}
