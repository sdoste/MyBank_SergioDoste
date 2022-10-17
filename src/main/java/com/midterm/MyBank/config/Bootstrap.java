package com.midterm.MyBank.config;

import com.midterm.MyBank.model.Users.Admin;
import com.midterm.MyBank.model.security.Role;
import com.midterm.MyBank.model.security.User;
import com.midterm.MyBank.repository.security.RoleRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Bootstrap implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()){
            User user = new Admin();
            user.setUsername("admin");
            // 123
            user.setPassword("$2a$10$gMtf7P0819c9cbs0NfR9FOI.ojQcLeh4UcxHkwSFzb7WSRNlY5mgG");

            Role role = new Role();
            role.setName("ADMIN");
            user.setRoles(Set.of(role));

            userRepository.save(user);
        }
    }
}
