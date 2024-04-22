package org.example.account.controller;

import lombok.RequiredArgsConstructor;
import org.example.account.domain.Account;
import org.example.account.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/create-account")
    public String createAccount() {
        accountService.createAccount();
        return "success";
    }

    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable("id") Long id) {
        return accountService.getAccount(id);
    }
}
