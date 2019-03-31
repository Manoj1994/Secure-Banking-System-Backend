package com.bankingapp.service.adminlogservice;

import com.bankingapp.model.employee.AdminLog;
import com.bankingapp.repository.adminlogrepository.AdminLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AdminLogService {

    @Autowired
    AdminLogRepository adminLogRepository;

    public Boolean save(AdminLog adminLog) {

        try {
            System.out.println(adminLog);
            adminLogRepository.save(adminLog);
            return true;
        } catch(Exception e) {

        }
        return false;
    }

    public List<AdminLog> getLogs() {
        return adminLogRepository.findAll();
    }

    public AdminLog buildLog(String message) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        AdminLog adminLog = new AdminLog();
        adminLog.setLog_timestamp(timestamp);
        adminLog.setMessage(message);
        return adminLog;
    }

    public Boolean createUserLog(int user_id, String message) {

        try {
            AdminLog adminLog = buildLog(message);
            adminLog.setRelated_user_id(user_id);
            return save(adminLog);
        } catch(Exception e) {

        }
        return  false;
    }

    public Boolean createEmployeeLog(int employee_id, String message) {

        try {
            AdminLog adminLog = buildLog(message);
            adminLog.setRelated_user_id(employee_id);
            save(adminLog);
            return true;
        } catch(Exception e) {

        }
        return  false;
    }

    public Boolean createUserEmployeeLog(int user_id, int employee_id, String message) {

        try {
            AdminLog adminLog = buildLog(message);
            adminLog.setRelated_user_id(user_id);
            adminLog.setRelated_employee_id(employee_id);
            save(adminLog);
            return true;
        } catch(Exception e) {

        }
        return  false;
    }
}
