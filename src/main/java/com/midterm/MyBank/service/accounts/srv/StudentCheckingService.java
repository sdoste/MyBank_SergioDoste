package com.midterm.MyBank.service.accounts.srv;

import com.midterm.MyBank.model.accounts.StudentChecking;

public interface StudentCheckingService {
    StudentChecking get(long id);
    StudentChecking save(StudentChecking studentChecking);
    StudentChecking update(StudentChecking studentChecking, long id);
    void delete(StudentChecking studentChecking);
}
