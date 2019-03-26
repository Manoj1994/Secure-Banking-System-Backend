package com.bankingapp.model.account;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int user_id;

    @Column(name ="name")
    private String name;

    @Column(name ="gender")
    private String gender;

    @Column(name ="dob")
    private String dob;

    @Column(name ="contact")
    private String contact;

    @Column(name ="email_id")
    private String email;

    @Column(name ="address")
    private String address;

    @Column(name ="user_type")
    private int user_type;

    @Column(name ="created")
    private Timestamp created;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getName().equals(customer.getName()) &&
                getContact().equals(customer.getContact()) &&
                getAddress().equals(customer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContact(), getAddress());
    }
}
