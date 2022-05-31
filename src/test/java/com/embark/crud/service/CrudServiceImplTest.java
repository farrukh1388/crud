package com.embark.crud.service;

import java.util.List;
import com.embark.crud.model.UserAccount;
import com.embark.crud.repository.CrudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrudServiceImplTest {

    private static final String ID = "1";

    @Mock
    private CrudRepository<UserAccount> userAccountCrudRepository;

    @InjectMocks
    private CrudServiceImpl<UserAccount> userAccountCrudService;

    private UserAccount testedAccount;

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
    void createAccount() {
        when(userAccountCrudRepository.createAccount(testedAccount)).thenReturn(testedAccount);

        UserAccount returnedAccount = userAccountCrudService.createAccount(testedAccount);

        assertEquals(testedAccount, returnedAccount);
        verify(userAccountCrudRepository, only()).createAccount(testedAccount);
    }

    @Test
    void readAccount() {
        when(userAccountCrudRepository.readAccount(ID)).thenReturn(testedAccount);

        UserAccount returnedAccount = userAccountCrudService.readAccount(ID);

        assertEquals(testedAccount, returnedAccount);
        verify(userAccountCrudRepository, only()).readAccount(ID);
    }

    @Test
    void updateAccount() {
        when(userAccountCrudRepository.updateAccount(testedAccount)).thenReturn(testedAccount);

        UserAccount returnedAccount = userAccountCrudService.updateAccount(testedAccount);

        assertEquals(testedAccount, returnedAccount);
        verify(userAccountCrudRepository, only()).updateAccount(testedAccount);
    }

    @Test
    void deleteAccount() {
        when(userAccountCrudRepository.deleteAccount(ID)).thenReturn(testedAccount);

        UserAccount returnedAccount = userAccountCrudService.deleteAccount(ID);

        assertEquals(testedAccount, returnedAccount);
        verify(userAccountCrudRepository, only()).deleteAccount(ID);
    }

    @Test
    void readAccountList() {
        List<UserAccount> accounts = List.of(testedAccount);

        when(userAccountCrudRepository.readAccountList(1, 3)).thenReturn(accounts);

        List<UserAccount> returnedAccounts = userAccountCrudService.readAccountList(1, 3);

        assertEquals(accounts, returnedAccounts);
        verify(userAccountCrudRepository, only()).readAccountList(1, 3);
    }
}
