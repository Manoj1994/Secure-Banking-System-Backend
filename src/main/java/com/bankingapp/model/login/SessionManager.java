package com.bankingapp.model.login;

import com.bankingapp.service.loginservice.SessionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimerTask;

public class SessionManager extends TimerTask {

    @Autowired
    SessionService sessionService;

    public void run() {
        System.out.print("Deleting Invalid Sessions");
        sessionService.deleteInvalidRows();
    }
}
