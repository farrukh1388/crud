package com.embark.crud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import com.embark.crud.exception.AccountAlreadyExistsException;
import com.embark.crud.exception.AccountNotFoundException;
import com.embark.crud.model.UserAccount;
import com.embark.crud.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@Api(tags = "Users")
@RequiredArgsConstructor
@Slf4j
public class UserAccountController {

    private final CrudService<UserAccount> crudService;

    @PostMapping
    @ApiOperation(value = "Create user account",
                  notes = "Creates new user account if it is not exists or returns HttpStatus.BAD_REQUEST if it exists")
    public ResponseEntity<UserAccount> createAccount(@RequestBody UserAccount userAccount) {
        if (userAccount.getId() == null) {
            log.error("Account id can't be null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok(crudService.createAccount(userAccount));
        } catch (AccountAlreadyExistsException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get user account",
                  notes = "Returns existing user account according to the provided user account id or returns HttpStatus.NOT_FOUND if account not found")
    public ResponseEntity<UserAccount> readAccount(@PathVariable final String id) {
        try {
            return ResponseEntity.ok(crudService.readAccount(id));
        } catch (AccountNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    @ApiOperation(value = "Update user account",
                  notes = "Updates user account if it exists or returns HttpStatus.NOT_FOUND if account not found")
    public ResponseEntity<UserAccount> updateAccount(@RequestBody UserAccount userAccount) {
        if (userAccount.getId() == null) {
            log.error("Account id can't be null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok(crudService.updateAccount(userAccount));
        } catch (AccountNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete user account",
                  notes = "Deletes existing user account according to the provided account id or returns HttpStatus.NOT_FOUND if account not found")
    public ResponseEntity<UserAccount> deleteAccount(@PathVariable final String id) {
        try {
            return ResponseEntity.ok(crudService.deleteAccount(id));
        } catch (AccountNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @ApiOperation(value = "Get user account list",
                  notes = "Returns user account list with pagination")
    public ResponseEntity<List<UserAccount>> readAccountList(@RequestParam final int page, @RequestParam final int size) {
        if (page < 1 || size < 0) {
            log.error("Page can't be less than 1 and size can't be less than 0");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(crudService.readAccountList(page, size));
    }
}
