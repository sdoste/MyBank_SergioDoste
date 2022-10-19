package com.midterm.MyBank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midterm.MyBank.model.Users.AccountHolder;
import com.midterm.MyBank.model.Utils.Address;
import com.midterm.MyBank.model.Utils.Money;
import com.midterm.MyBank.model.accounts.Checking;
import com.midterm.MyBank.model.security.Role;
import com.midterm.MyBank.repository.CheckingRepository;
import com.midterm.MyBank.repository.security.RoleRepository;
import com.midterm.MyBank.repository.security.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static com.midterm.MyBank.service.utils.PasswordUtil.encryptPassword;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
class CheckingControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private CheckingRepository checkingRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Address address = new Address("Spain", "Barcelona", "Calle Mallorca", "120" );
        LocalDate date1992 = LocalDate.of(1992, 1, 8);

        AccountHolder userFran = new AccountHolder("fran", encryptPassword("fran123"), "Francisco Tilleo", date1992, address);
        AccountHolder userMaria = new AccountHolder("Maria", encryptPassword("maria123"), "Maria Chuelo", date1992, address);

        //Assigning accountholder roles
        Role accountholder = new Role("ACCOUNTHOLDER");
        accountholder.setId(1L);
        roleRepository.save(accountholder);
        userFran.setRoles(Set.of(accountholder));

        userRepository.save(userFran);
        userRepository.save(userMaria);
        //id 1
        Checking userFranAccount = new Checking("123", userFran, new Money(new BigDecimal("1111")));
        //id 2
        Checking userMariaAccount = new Checking("123", userMaria, new Money(new BigDecimal("1111")));


        checkingRepository.save(userFranAccount);
        checkingRepository.save(userMariaAccount);
        System.out.println("hi");
    }

    @AfterEach
    void tearDown(){
        checkingRepository.deleteAll();
        checkingRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    @WithMockUser(username = "fran", password = "fran123", roles = {"ACCOUNTHOLDER"})
    void testGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/accounts/fran/checking/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Checking checkingAccount = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Checking.class);

        assertEquals("Francisco Tilleo", checkingAccount.getPrimaryOwner().getName());
        assertEquals("fran", checkingAccount.getPrimaryOwner().getUsername());
        }
    }
