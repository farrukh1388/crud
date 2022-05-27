package com.embark.crud.repository;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import com.embark.crud.exception.AccountAlreadyExistsException;
import com.embark.crud.exception.AccountNotFoundException;
import com.embark.crud.model.UserAccount;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserAccountRepositoryImpl implements CrudRepository<UserAccount> {

    private final Map<String, UserAccount> cache;

    public UserAccountRepositoryImpl() {
        this.cache = initCache();
    }

    @Override
    public UserAccount createAccount(UserAccount userAccount) {
        String id = userAccount.getId();
        if (cache.containsKey(id)) {
            String errorMessage = String.format("Account with id %s already exists", id);
            throw new AccountAlreadyExistsException(errorMessage);
        }
        log.info("Creating user account with id {}", id);
        cache.put(id, userAccount);
        return userAccount;
    }

    @Override
    public UserAccount readAccount(String id) {
        if (cache.containsKey(id)) {
            log.info("Getting user account with id {}", id);
            return cache.get(id);
        }
        String errorMessage = String.format("Account with id %s not found", id);
        throw new AccountNotFoundException(errorMessage);
    }

    @Override
    public UserAccount updateAccount(UserAccount userAccount) {
        String id = userAccount.getId();
        if (cache.containsKey(id)) {
            log.info("Updating user account with id {}", id);
            return cache.replace(id, userAccount);
        }
        String errorMessage = String.format("Account with id %s not found", id);
        throw new AccountNotFoundException(errorMessage);
    }

    @Override
    public UserAccount deleteAccount(String id) {
        if (cache.containsKey(id)) {
            log.info("Deleting user account with id {}", id);
            return cache.remove(id);
        }
        String errorMessage = String.format("Account with id %s not found", id);
        throw new AccountNotFoundException(errorMessage);
    }

    @Override
    public List<UserAccount> readAccountList(int page, int size) {
        log.info("Getting user account list for page {} with size {}", page, size);
        List<UserAccount> allValues = new ArrayList<>(cache.values());
        if (allValues.size() < (page - 1) * size) {
            return readAccountList(page - 1, size);
        }
        if (allValues.size() < page * size) {
            return allValues.subList((page - 1) * size, allValues.size());
        }
        return allValues.subList((page - 1) * size, page * size);
    }

    private Map<String, UserAccount> initCache() {
        Map<String, UserAccount> cache = new ConcurrentSkipListMap<>();
        UserAccount firstAccount = UserAccount.builder()
                .id("1")
                .email("first@mail.com")
                .username("first")
                .password("firstPassword".toCharArray())
                .build();
        UserAccount secondAccount = UserAccount.builder()
                .id("2")
                .email("second@mail.com")
                .username("second")
                .password("secondPassword".toCharArray())
                .build();
        UserAccount thirdAccount = UserAccount.builder()
                .id("3")
                .email("third@mail.com")
                .username("third")
                .password("thirdPassword".toCharArray())
                .build();
        UserAccount fourthAccount = UserAccount.builder()
                .id("4")
                .email("fourth@mail.com")
                .username("fourth")
                .password("fourthPassword".toCharArray())
                .build();
        UserAccount fifthAccount = UserAccount.builder()
                .id("5")
                .email("fifth@mail.com")
                .username("fifth")
                .password("fifthPassword".toCharArray())
                .build();
        cache.put("1", firstAccount);
        cache.put("2", secondAccount);
        cache.put("3", thirdAccount);
        cache.put("4", fourthAccount);
        cache.put("5", fifthAccount);
        return cache;
    }
}
