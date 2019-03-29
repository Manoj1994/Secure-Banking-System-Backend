package com.bankingapp.model.login;

import javax.persistence.*;
import java.sql.Timestamp;

//drop table if exists securebank.session;
//        CREATE TABLE securebank.session (
//        id bigint(16) NOT NULL AUTO_INCREMENT,
//        timestamp_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
//        username varchar(255) NOT NULL,
//        password varchar(255) NOT NULL,
//        status boolean NOT NULL,
//        access_key varchar(255) NOT NULL,
//        access_right int(11),
//        PRIMARY KEY(id)
//        );

@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "timestamp_created")
    private Timestamp timestamp_created;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private boolean status;

    @Column(name = "access_key")
    private String access_key;

    @Column(name = "access_right")
    private int access_right;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp_created() {
        return timestamp_created;
    }

    public void setTimestamp_created(Timestamp timestamp_created) {
        this.timestamp_created = timestamp_created;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public int getAccess_right() {
        return access_right;
    }

    public void setAccess_right(int access_right) {
        this.access_right = access_right;
    }
}
