package com.example.sns.service;


import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;


    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    public void 회원가입_정상_동작() throws Exception {
        String userName = "userName";
        String password = "password";

        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }
}
