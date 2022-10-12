package com.midterm.MyBank.model.Users;

import com.midterm.MyBank.model.security.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ThirdParty extends User {
    @Id
    @GeneratedValue
    private long id;

    private String name;

}
