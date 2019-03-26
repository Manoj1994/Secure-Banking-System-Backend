package com.bankingapp.model.request;

import javax.persistence.*;

@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name = "requester_id", updatable = false, nullable = false)
    private int requester_id;

    @Column(name = "approver_id")
    private int approver_id;

    @Column(name = "request_type")
    private String request_type;

    @Column(name = "requested_value")
    private String requested_value;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequesterId()  {
        return requester_id;
    }

    public void setRequesterId(int requesterid) {
        this.requester_id = requesterid;
    }

    public int getApproverId() {
        return approver_id;
    }

    public void setApproverId(int a_id)
    {
        approver_id = a_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getRequested_value() {
        return requested_value;
    }

    public void setRequested_value(String requested_value) {
        this.requested_value = requested_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
