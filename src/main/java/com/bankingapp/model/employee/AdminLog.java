package com.bankingapp.model.employee;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "admin_log")
public class AdminLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "log_timestamp")
    private Timestamp log_timestamp;

    @Column(name = "related_user_id")
    private int related_user_id;

    @Column(name = "related_employee_id")
    private int related_employee_id;

    @Column(name = "message")
    private String message;

    @Override
    public String toString() {
        return "AdminLog{" +
                "id=" + id +
                ", log_timestamp=" + log_timestamp +
                ", related_user_id=" + related_user_id +
                ", related_employee_id=" + related_employee_id +
                ", message='" + message + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getLog_timestamp() {
        return log_timestamp;
    }

    public void setLog_timestamp(Timestamp log_timestamp) {
        this.log_timestamp = log_timestamp;
    }

    public int getRelated_user_id() {
        return related_user_id;
    }

    public void setRelated_user_id(int related_user_id) {
        this.related_user_id = related_user_id;
    }

    public int getRelated_employee_id() {
        return related_employee_id;
    }

    public void setRelated_employee_id(int related_employee_id) {
        this.related_employee_id = related_employee_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
