package com.midterm.MyBank.model.Users;

import com.midterm.MyBank.model.security.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Admin extends User {

    private String name;

    //constructors
    public Admin(){

    }
    public Admin(String username, String password, String name) {
        super(username, password);
        this.name = name;
    }
    public Admin(String name){
        this.name = name;
    }
}
