package com.midterm.MyBank.repository.security;

import com.midterm.MyBank.model.security.Role;
import com.midterm.MyBank.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
