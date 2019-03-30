package com.bankingapp.model.login;

public class LoginResponse {

    private String name;

    private int role;

    private int id;

    private String message = "Logged In";

    private boolean success;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", id=" + id +
                ", message='" + message + '\'' +
                '}';
    }

    public LoginResponse(boolean success, String name, int role, int id, String message) {
        this.success = success;
        this.name = name;
        this.role = role;
        this.id = id;
        this.message = message;
    }

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessgae(String messgae) {
        this.message = messgae;
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
