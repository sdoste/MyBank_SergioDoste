package com.midterm.MyBank.service.accounts;

import com.midterm.MyBank.model.accounts.StudentChecking;
import com.midterm.MyBank.repository.StudentCheckingRepository;
import com.midterm.MyBank.service.accounts.interfaces.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentCheckingServiceImpl implements StudentCheckingService {
    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Override
    public StudentChecking get(long id) {
        return studentCheckingRepository.findById(id).get();
    }

    @Override
    public StudentChecking save(StudentChecking studentChecking) {
        return studentCheckingRepository.save(studentChecking);
    }

    @Override
    public StudentChecking update(StudentChecking studentChecking, long id) {
        StudentChecking recoveredObject = studentCheckingRepository.findById(id).get();
        recoveredObject.setBalance(studentChecking.getBalance());
        return studentCheckingRepository.save(recoveredObject);
    }

    @Override
    public void delete(StudentChecking studentChecking) {
        studentCheckingRepository.delete(studentChecking);
    }
}
