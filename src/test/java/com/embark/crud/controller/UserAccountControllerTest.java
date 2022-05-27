package com.embark.crud.controller;

import java.util.List;
import com.embark.crud.exception.AccountAlreadyExistsException;
import com.embark.crud.exception.AccountNotFoundException;
import com.embark.crud.model.UserAccount;
import com.embark.crud.service.CrudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserAccountController.class)
class UserAccountControllerTest {

    private static final String ID = "1";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CrudService<UserAccount> crudService;

    private UserAccount testedAccount;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testedAccount = UserAccount.builder()
                .id(ID)
                .email("first@mail.com")
                .username("first")
                .password("firstPassword".toCharArray())
                .build();
    }

    @Test
    void createAccount_whenUserAccountIsAbsent_returnsBadRequestStatus() throws Exception {
        mvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_whenUserAccountIdIsNull_returnsBadRequestStatus() throws Exception {
        testedAccount.setId(null);

        mvc.perform(post("/users")
                            .content(objectMapper.writeValueAsString(testedAccount))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_whenRequestIsCorrect_returnsOkStatus() throws Exception {
        Mockito.when(crudService.createAccount(Mockito.any(UserAccount.class))).thenReturn(testedAccount);

        mvc.perform(post("/users")
                            .content(objectMapper.writeValueAsString(testedAccount))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testedAccount.getId())))
                .andExpect(jsonPath("$.email", is(testedAccount.getEmail())))
                .andExpect(jsonPath("$.username", is(testedAccount.getUsername())))
                .andExpect(jsonPath("$.password", is(new String(testedAccount.getPassword()))));
    }

    @Test
    void createAccount_whenServiceThrowsAccountAlreadyExistsException_returnsBadRequestStatus() throws Exception {
        Mockito.when(crudService.createAccount(Mockito.any(UserAccount.class))).thenThrow(AccountAlreadyExistsException.class);

        mvc.perform(post("/users")
                            .content(objectMapper.writeValueAsString(testedAccount))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void readAccount_whenRequestIsCorrect_returnsOkStatus() throws Exception {
        Mockito.when(crudService.readAccount("1")).thenReturn(testedAccount);

        mvc.perform(get("/users/1")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testedAccount.getId())))
                .andExpect(jsonPath("$.email", is(testedAccount.getEmail())))
                .andExpect(jsonPath("$.username", is(testedAccount.getUsername())))
                .andExpect(jsonPath("$.password", is(new String(testedAccount.getPassword()))));
    }

    @Test
    void readAccount_whenServiceThrowsAccountNotFoundException_returnsNotFoundStatus() throws Exception {
        Mockito.when(crudService.readAccount("1")).thenThrow(AccountNotFoundException.class);

        mvc.perform(get("/users/1")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_whenUserAccountIsAbsent_returnsBadRequestStatus() throws Exception {
        mvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccount_whenUserAccountIdIsNull_returnsBadRequestStatus() throws Exception {
        testedAccount.setId(null);

        mvc.perform(put("/users")
                            .content(objectMapper.writeValueAsString(testedAccount))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccount_whenRequestIsCorrect_returnsOkStatus() throws Exception {
        Mockito.when(crudService.updateAccount(Mockito.any(UserAccount.class))).thenReturn(testedAccount);

        mvc.perform(put("/users")
                            .content(objectMapper.writeValueAsString(testedAccount))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testedAccount.getId())))
                .andExpect(jsonPath("$.email", is(testedAccount.getEmail())))
                .andExpect(jsonPath("$.username", is(testedAccount.getUsername())))
                .andExpect(jsonPath("$.password", is(new String(testedAccount.getPassword()))));
    }

    @Test
    void updateAccount_whenServiceThrowsAccountNotFoundException_returnsNotFoundStatus() throws Exception {
        Mockito.when(crudService.updateAccount(Mockito.any(UserAccount.class))).thenThrow(AccountNotFoundException.class);

        mvc.perform(put("/users")
                            .content(objectMapper.writeValueAsString(testedAccount))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_whenRequestIsCorrect_returnsOkStatus() throws Exception {
        Mockito.when(crudService.deleteAccount("1")).thenReturn(testedAccount);

        mvc.perform(delete("/users/1")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testedAccount.getId())))
                .andExpect(jsonPath("$.email", is(testedAccount.getEmail())))
                .andExpect(jsonPath("$.username", is(testedAccount.getUsername())))
                .andExpect(jsonPath("$.password", is(new String(testedAccount.getPassword()))));
    }

    @Test
    void deleteAccount_whenServiceThrowsAccountNotFoundException_returnsNotFoundStatus() throws Exception {
        Mockito.when(crudService.deleteAccount("1")).thenThrow(AccountNotFoundException.class);

        mvc.perform(delete("/users/1")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void readAccountList_whenPageCountIsLessThanOne_returnsBadRequestStatus() throws Exception {
        mvc.perform(get("/users/list?page=0&size=1")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void readAccountList_whenPageSizeIsLessThanZero_returnsBadRequestStatus() throws Exception {
        mvc.perform(get("/users/list?page=1&size=-3")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void readAccountList_whenRequestIsCorrect_returnsOkStatus() throws Exception {
        List<UserAccount> accountList = List.of(testedAccount);

        Mockito.when(crudService.readAccountList(1, 1)).thenReturn(accountList);

        mvc.perform(get("/users/list?page=1&size=1")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testedAccount.getId())))
                .andExpect(jsonPath("$[0].email", is(testedAccount.getEmail())))
                .andExpect(jsonPath("$[0].username", is(testedAccount.getUsername())))
                .andExpect(jsonPath("$[0].password", is(new String(testedAccount.getPassword()))));
    }
}
