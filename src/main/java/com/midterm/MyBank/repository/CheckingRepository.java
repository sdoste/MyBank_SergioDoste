package com.midterm.MyBank.repository;

import com.midterm.MyBank.model.accounts.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {
    List<Checking> findAllByPrimaryOwnerId(long id);
}
