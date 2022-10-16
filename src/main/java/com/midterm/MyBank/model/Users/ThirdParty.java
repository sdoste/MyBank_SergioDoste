package com.midterm.MyBank.model.Users;

import com.midterm.MyBank.model.security.User;

import javax.persistence.Entity;

@Entity
public class ThirdParty extends User {

    private String name;
    //naming it securityKey because otherwise it clashes with SQL specific language
    private String securityKey;

    //getters and setters


    public ThirdParty(String username, String password, String name, String securityKey) {
        super(username, password);
        this.name = name;
        setSecurityKey(securityKey);
    }

    public ThirdParty() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String key) {
        this.securityKey = key;
    }
}
