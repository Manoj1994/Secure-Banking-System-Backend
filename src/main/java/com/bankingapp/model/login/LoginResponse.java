package com.bankingapp.model.login;

public class LoginResponse {

    private String name;

    private int role;

    private int id;

    public LoginResponse() {

    }

    public LoginResponse(String userName, int role, int id) {
        this.name = userName;
        this.role = role;
        this.id = id;
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
