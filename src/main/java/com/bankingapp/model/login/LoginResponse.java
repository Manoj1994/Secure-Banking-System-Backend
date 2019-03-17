package com.bankingapp.model.login;

public class LoginResponse {

    private String userName;

    private int role;

    private int id;

    public LoginResponse() {

    }

    public LoginResponse(String userName, int role, int id) {
        this.userName = userName;
        this.role = role;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
