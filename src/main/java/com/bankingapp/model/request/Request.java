package com.bankingapp.model.request;

public class Request {
    private int id;
    private int requester_id;
    private int account_id;
    private int approver_id;
    private String request_type;
    private String current_value;
    private String requested_value;
    private String status;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequesterId() {
        return requester_id;
    }

    public void setRequesterId(int requesterid) {
        this.requester_id = requesterid;
    }

    public int getAccountId() {
        return account_id;
    }

    public void setAccountId(int acc_id) {
        account_id = acc_id;
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

    public String getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
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
