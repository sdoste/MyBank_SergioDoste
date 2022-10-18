package com.midterm.MyBank.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.midterm.MyBank.model.accounts.Account;

import javax.persistence.*;
import java.util.Set;

@Entity
public abstract class User extends Account {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected long id;
    @Column(unique = true)
    private String username;
    private String password;
//    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "users&roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//
//    )
//    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public long getId() {
        return id;
    }
}
