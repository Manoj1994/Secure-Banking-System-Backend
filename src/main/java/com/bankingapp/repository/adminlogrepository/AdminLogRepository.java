package com.bankingapp.repository.adminlogrepository;

import com.bankingapp.model.employee.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {

}
