package com.bankingapp.model.employee;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private int employee_id;

    @Column(name = "employee_name")
    private String employee_name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private int age;

    @Column(name = "tier_level")
    private int tier_level;

    @Column(name = "designation_id")
    private int designation_id;

    @Column(name = "contact_no")
    private String contact_no;

    @Column(name = "email_id")
    private String email_id;

    @Column(name = "address")
    private String address;

    @Column(name = "created")
    private Timestamp created;

    @Override
    public String toString() {
        return "Employee{" +
                "employee_id=" + employee_id +
                ", employee_name='" + employee_name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", tier_level=" + tier_level +
                ", designation_id=" + designation_id +
                ", contact_no='" + contact_no + '\'' +
                ", email_id='" + email_id + '\'' +
                ", address='" + address + '\'' +
                ", created=" + created +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return getAge() == employee.getAge() &&
                getTier_level() == employee.getTier_level() &&
                getDesignation_id() == employee.getDesignation_id() &&
                Objects.equals(getEmployee_name(), employee.getEmployee_name()) &&
                Objects.equals(getGender(), employee.getGender()) &&
                Objects.equals(getContact_no(), employee.getContact_no()) &&
                Objects.equals(getEmail_id(), employee.getEmail_id()) &&
                Objects.equals(getAddress(), employee.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployee_name(), getGender(), getAge(), getTier_level(), getDesignation_id(), getContact_no(), getEmail_id(), getAddress());
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTier_level() {
        return tier_level;
    }

    public void setTier_level(int tier_level) {
        this.tier_level = tier_level;
    }

    public int getDesignation_id() {
        return designation_id;
    }

    public void setDesignation_id(int designation_id) {
        this.designation_id = designation_id;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
