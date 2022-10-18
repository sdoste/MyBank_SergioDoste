package com.midterm.MyBank.repository;

import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findAllByPrimaryOwnerId(long id);
}
