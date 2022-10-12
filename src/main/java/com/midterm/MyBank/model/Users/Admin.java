package com.midterm.MyBank.model.Users;

import com.midterm.MyBank.model.security.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Admin extends User {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    //constructors
    public Admin(){

    }
    public Admin(String name){
        this.name = name;
    }
}
