package com.embark.crud.repository;

import java.util.Arrays;
import java.util.List;
import com.embark.crud.exception.AccountAlreadyExistsException;
import com.embark.crud.exception.AccountNotFoundException;
import com.embark.crud.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserAccountRepositoryImplTest {

    private UserAccountRepositoryImpl userAccountRepository;

    @BeforeEach
    void setUp() {
        userAccountRepository = new UserAccountRepositoryImpl();
    }

    @Test
    void createAccount_whenAccountExists_throwsAccountAlreadyExistsException() {
        UserAccount existingAccount = UserAccount.builder()
                .id("1")
                .build();

        AccountAlreadyExistsException thrown = assertThrows(
                AccountAlreadyExistsException.class,
                () -> userAccountRepository.createAccount(existingAccount)
        );

        assertEquals("Account with id 1 already exists", thrown.getMessage());
    }

    @Test
    void createAccount_whenAccountNew_returnsCreatedAccount() {
        UserAccount newAccount = UserAccount.builder()
                .id("6")
                .email("sixth@mail.com")
                .username("sixth")
                .password("sixthPassword".toCharArray())
                .build();

        UserAccount returnedAccount = userAccountRepository.createAccount(newAccount);

        assertEquals(newAccount, returnedAccount);
    }

    @Test
    void readAccount_whenCacheContainsRequiredAccount_returnsRequiredAccount() {
        UserAccount newAccount = UserAccount.builder()
                .id("6")
                .email("sixth@mail.com")
                .username("sixth")
                .password("sixthPassword".toCharArray())
                .build();
        userAccountRepository.createAccount(newAccount);

        assertEquals(newAccount, userAccountRepository.readAccount("6"));
    }

    @Test
    void readAccount_whenAccountNotFound_throwsAccountNotFoundException() {

        AccountNotFoundException thrown = assertThrows(
                AccountNotFoundException.class,
                () -> userAccountRepository.readAccount("6")
        );

        assertEquals("Account with id 6 not found", thrown.getMessage());
    }

    @Test
    void updateAccount_whenAccountExists_returnsPreviousAccount() {
        UserAccount accountToUpdate = UserAccount.builder()
                .id("1")
                .email("firstUpdated@mail.com")
                .username("firstUpdated")
                .password("firstUpdatedPassword".toCharArray())
                .build();

        UserAccount returnedAccount = userAccountRepository.updateAccount(accountToUpdate);

        assertFirstAccount(returnedAccount);
        UserAccount accountFromCache = userAccountRepository.readAccount("1");
        assertEquals(accountToUpdate, accountFromCache);
    }

    @Test
    void updateAccount_whenAccountNotFound_throwsAccountNotFoundException() {
        UserAccount accountToUpdate = UserAccount.builder()
                .id("6")
                .build();

        AccountNotFoundException thrown = assertThrows(
                AccountNotFoundException.class,
                () -> userAccountRepository.updateAccount(accountToUpdate)
        );

        assertEquals("Account with id 6 not found", thrown.getMessage());
    }

    @Test
    void deleteAccount_whenAccountExists_returnsDeletedPreviousAccount() {

        UserAccount returnedAccount = userAccountRepository.deleteAccount("1");

        assertFirstAccount(returnedAccount);
        assertThrows(
                AccountNotFoundException.class,
                () -> userAccountRepository.readAccount("1")
        );
    }

    @Test
    void deleteAccount_whenAccountNotFound_throwsAccountNotFoundException() {
        AccountNotFoundException thrown = assertThrows(
                AccountNotFoundException.class,
                () -> userAccountRepository.deleteAccount("6")
        );

        assertEquals("Account with id 6 not found", thrown.getMessage());
    }

    @Test
    void readAccountList_returnsAccountsForFirstPage() {
        List<UserAccount> accounts = userAccountRepository.readAccountList(1, 3);

        assertNotNull(accounts);
        assertEquals(3, accounts.size());
        assertFirstAccount(accounts.get(0));
        assertSecondAccount(accounts.get(1));
        assertThirdAccount(accounts.get(2));
    }

    @Test
    void readAccountList_returnsAccountsForSecondPage() {
        List<UserAccount> accounts = userAccountRepository.readAccountList(2, 3);

        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertFourthAccount(accounts.get(0));
        assertFifthAccount(accounts.get(1));
    }

    @Test
    void readAccountList_whenPageNumberIsTooHigh_returnsAccountsForLastPage() {
        List<UserAccount> accounts = userAccountRepository.readAccountList(20, 3);

        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertFourthAccount(accounts.get(0));
        assertFifthAccount(accounts.get(1));
    }

    @Test
    void initCache() {
        assertFirstAccount(userAccountRepository.readAccount("1"));
        assertSecondAccount(userAccountRepository.readAccount("2"));
        assertThirdAccount(userAccountRepository.readAccount("3"));
        assertFourthAccount(userAccountRepository.readAccount("4"));
        assertFifthAccount(userAccountRepository.readAccount("5"));
    }

    private void assertFirstAccount(UserAccount firstAccount) {
        assertNotNull(firstAccount);
        assertEquals("1", firstAccount.getId());
        assertEquals("first@mail.com", firstAccount.getEmail());
        assertEquals("first", firstAccount.getUsername());
        assertEquals("[f, i, r, s, t, P, a, s, s, w, o, r, d]", Arrays.toString(firstAccount.getPassword()));
    }

    private void assertSecondAccount(UserAccount secondAccount) {
        assertNotNull(secondAccount);
        assertEquals("2", secondAccount.getId());
        assertEquals("second@mail.com", secondAccount.getEmail());
        assertEquals("second", secondAccount.getUsername());
        assertEquals("[s, e, c, o, n, d, P, a, s, s, w, o, r, d]", Arrays.toString(secondAccount.getPassword()));
    }

    private void assertThirdAccount(UserAccount thirdAccount) {
        assertNotNull(thirdAccount);
        assertEquals("3", thirdAccount.getId());
        assertEquals("third@mail.com", thirdAccount.getEmail());
        assertEquals("third", thirdAccount.getUsername());
        assertEquals("[t, h, i, r, d, P, a, s, s, w, o, r, d]", Arrays.toString(thirdAccount.getPassword()));
    }

    private void assertFourthAccount(UserAccount fourthAccount) {
        assertNotNull(fourthAccount);
        assertEquals("4", fourthAccount.getId());
        assertEquals("fourth@mail.com", fourthAccount.getEmail());
        assertEquals("fourth", fourthAccount.getUsername());
        assertEquals("[f, o, u, r, t, h, P, a, s, s, w, o, r, d]", Arrays.toString(fourthAccount.getPassword()));
    }

    private void assertFifthAccount(UserAccount fifthAccount) {
        assertNotNull(fifthAccount);
        assertEquals("5", fifthAccount.getId());
        assertEquals("fifth@mail.com", fifthAccount.getEmail());
        assertEquals("fifth", fifthAccount.getUsername());
        assertEquals("[f, i, f, t, h, P, a, s, s, w, o, r, d]", Arrays.toString(fifthAccount.getPassword()));
    }
}
