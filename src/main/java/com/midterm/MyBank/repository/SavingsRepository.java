package com.midterm.MyBank.repository;

import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {
    List<Savings> findAllByPrimaryOwnerId(long id);
}
