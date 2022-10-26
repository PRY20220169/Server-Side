package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    Page<Account> getAllAccounts(Pageable pageable);
    Account getAccountById(Long accountId);
    Account createAccount(Long userId, Account account);
    Account updateAccount(Long userId, Account accountRequest);
    ResponseEntity<?> deleteAccount(Long userId);
}
