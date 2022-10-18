package com.midterm.MyBank.repository;

import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {
    List<Checking> findAllByPrimaryOwnerId(long id);
}
