package com.bankingapp.model.account;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "customer")
public class Customer implements Serializable,Cloneable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getUser_id() == customer.getUser_id() &&
                getUser_type() == customer.getUser_type() &&
                getName().equals(customer.getName()) &&
                getGender().equals(customer.getGender()) &&
                getDob().equals(customer.getDob()) &&
                getContact().equals(customer.getContact()) &&
                getEmail().equals(customer.getEmail()) &&
                getAddress().equals(customer.getAddress());
    }

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

    public boolean shortEquals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getName(), customer.getName()) &&
                Objects.equals(getContact(), customer.getContact()) &&
                Objects.equals(getEmail(), customer.getEmail()) &&
                Objects.equals(getAddress(), customer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContact(), getEmail(), getAddress());
    }

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", user_type=" + user_type +
                ", created=" + created +
                '}';
    }
}
