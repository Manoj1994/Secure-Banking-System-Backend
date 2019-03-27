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

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "related_user_id")
    private int related_user_id;

    @Column(name = "related_employee_id")
    private int related_employee_id;

    @Column(name = "message")
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
