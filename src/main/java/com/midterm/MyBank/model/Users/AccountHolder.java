package com.midterm.MyBank.model.Users;

import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.security.User;
import com.midterm.MyBank.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Entity
public class AccountHolder extends User {


    @Id
    @GeneratedValue
    private long id;
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

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress){
        this.name = name;
        setDateOfBirth(dateOfBirth);
        this.primaryAddress = primaryAddress;
    }
    //constructor with mailingAddress
    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress){
        this.name = name;
        setDateOfBirth(dateOfBirth);
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    //getters and setters

    public long getId() {
        return id;
    }
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
