package com.midterm.MyBank.repository;

import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.accounts.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, Long> {
    List<StudentChecking> findAllByPrimaryOwnerId(long id);
}
