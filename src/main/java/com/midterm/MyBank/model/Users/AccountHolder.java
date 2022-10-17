package com.midterm.MyBank.model.Users;

import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.security.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class AccountHolder extends User {

    private String name;
    private LocalDate dateOfBirth;
    @Embedded
    private Address primaryAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "country", column = @Column(name = "mailing_address_country")),
            @AttributeOverride( name = "city", column = @Column(name = "mailing_address_city")),
            @AttributeOverride( name = "line1", column = @Column(name = "mailing_address_line1")),
            @AttributeOverride( name = "line2", column = @Column(name = "mailing_address_line2")),
    })
    private Address mailingAddress;

    //constructors
       public AccountHolder() {
    }

    public AccountHolder(String username, String password, String name, LocalDate dateOfBirth, Address primaryAddress){
        super(username, password);
        this.name = name;
        setDateOfBirth(dateOfBirth);
        this.primaryAddress = primaryAddress;
    }
    //constructor with mailingAddress
    public AccountHolder(String username, String password, String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress){
        super(username, password);
        this.name = name;
        setDateOfBirth(dateOfBirth);
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    //getters and setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public Address getPrimaryAddress() {
        return primaryAddress;
    }
    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }
    public Address getMailingAddress() {
        return mailingAddress;
    }
    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }


}
