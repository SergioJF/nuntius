package com.nuntius.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuntius.account.AccountServiceApplication;
import com.nuntius.account.domain.Account;
import com.nuntius.account.domain.User;
import com.nuntius.account.service.AccountService;
import com.sun.security.auth.UserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sergiojimenezfemenia on 11/7/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccountServiceApplication.class)
public class AccountControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void shouldGetAccountByName() throws Exception {

        final Account account = new Account();
        account.setName("test");

        when(accountService.findByName(account.getName())).thenReturn(account);

        mockMvc.perform(get("/" + account.getName()))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldGetCurrentAccount() throws Exception {

        final Account account = new Account();
        account.setName("test");

        when(accountService.findByName(account.getName())).thenReturn(account);

        mockMvc.perform(get("/current").principal(new UserPrincipal(account.getName())))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSaveCurrentAccount() throws Exception {

        Account account = new Account();
        account.setName("test");
        account.setMail("mailtest");
        account.setIcon("icontest");
        account.setLastSeen(new Date());

        String json = mapper.writeValueAsString(account);

        mockMvc.perform(put("/current").principal(new UserPrincipal(account.getName())).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFailOnValidationTryingToSaveCurrentAccount() throws Exception {

        final Account account = new Account();
        account.setName("test");

        String json = mapper.writeValueAsString(account);

        mockMvc.perform(put("/current").principal(new UserPrincipal(account.getName())).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRegisterNewAccount() throws Exception {

        final User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        String json = mapper.writeValueAsString(user);
        System.out.println(json);
        mockMvc.perform(post("/").principal(new UserPrincipal("test")).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldFailOnValidationTryingToRegisterNewAccount() throws Exception {

        final User user = new User();
        user.setUsername("t");

        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/").principal(new UserPrincipal("test")).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

}
