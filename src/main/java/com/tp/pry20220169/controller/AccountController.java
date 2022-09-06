package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Account;
import com.tp.pry20220169.domain.service.AccountService;
import com.tp.pry20220169.resource.SaveAccountResource;
import com.tp.pry20220169.resource.AccountResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Accounts", description = "Accounts API")
@RestController
@RequestMapping("/api")
@CrossOrigin
public class AccountController {
    
    @Autowired
    private ModelMapper mapper;
    
    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public Page<AccountResource> getAllAccounts(Pageable pageable){
        Page<Account> accountPage = accountService.getAllAccounts(pageable);
        List<AccountResource> resources = accountPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/users/{userId}/account")
    public AccountResource getAccountByUserId(@PathVariable(name = "userId") Long accountId){
        return convertToResource(accountService.getAccountById(accountId));
    }

    @PostMapping("/users/{userId}/account")
    public AccountResource createAccount(@PathVariable(name = "userId") Long userId, @Valid @RequestBody SaveAccountResource resource){
        Account account = convertToEntity(resource);
        return convertToResource(accountService.createAccount(userId, account));
    }

    @PutMapping("/users/{userId}/account")
    public AccountResource updateAccount(@PathVariable(name = "userId") Long accountId,
                                   @Valid @RequestBody SaveAccountResource resource){
        Account account = convertToEntity(resource);
        return convertToResource(accountService.updateAccount(accountId, account));
    }

    @DeleteMapping("/users/{userId}/account")
    public ResponseEntity<?> deleteAccount(@PathVariable(name = "userId") Long userId){
        return accountService.deleteAccount(userId);
    }

    private Account convertToEntity(SaveAccountResource resource) { return mapper.map(resource, Account.class); }
    private AccountResource convertToResource(Account entity) { return mapper.map(entity, AccountResource.class); }
}
