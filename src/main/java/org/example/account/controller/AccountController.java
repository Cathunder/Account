package org.example.account.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.account.domain.Account;
import org.example.account.dto.CreateAccount;
import org.example.account.service.AccountService;
import org.example.account.service.RedisTestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final RedisTestService redisTestService;

    @PostMapping("/account")
    public CreateAccount.Response createAccount(
                @RequestBody
                @Valid // 객체 유효성 검사
                CreateAccount.Request request
            ) {
        accountService.createAccount(
                request.getUserId(),
                request.getInitialBalance()
        );

        return null;
    }

    @GetMapping("/get-lock")
    public String getLock() {
        return redisTestService.getLock();
    }


    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable("id") Long id) {
        return accountService.getAccount(id);
    }
}
