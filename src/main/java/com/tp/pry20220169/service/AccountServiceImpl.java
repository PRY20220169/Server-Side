package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Account;
import com.tp.pry20220169.domain.repository.AccountRepository;
import com.tp.pry20220169.domain.repository.UserRepository;
import com.tp.pry20220169.domain.service.AccountService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Id", accountId));
    }

    @Override
    public Account createAccount(Long userId, Account account) {
        if(accountRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Account already exists for user with Id: " + userId);
        }
        return userRepository.findById(userId).map(user -> {
            account.setUser(user);
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
    }

    @Override
    public Account updateAccount(Long accountId, Account accountRequest) {
        return accountRepository.findById(accountId).map(account -> {
            account.setFirstName(accountRequest.getFirstName());
            account.setLastName(accountRequest.getLastName());
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("Account", "Id", accountId));
    }

    @Override
    public ResponseEntity<?> deleteAccount(Long userId) {
        return accountRepository.findById(userId).map(account -> {
            accountRepository.delete(account);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
    }
}
