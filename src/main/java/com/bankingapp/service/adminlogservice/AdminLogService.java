package com.bankingapp.service.adminlogservice;

import com.bankingapp.model.employee.AdminLog;
import com.bankingapp.repository.adminlogrepository.AdminLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminLogService {

    @Autowired
    AdminLogRepository adminLogRepository;

    public Boolean save(AdminLog adminLog) {

        try {
            adminLogRepository.save(adminLog);
            return true;
        } catch(Exception e) {

        }
        return false;
    }
}
