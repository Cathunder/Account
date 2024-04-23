package org.example.account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.account.domain.Account;
import org.example.account.domain.AccountStatus;
import org.example.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    // 생성자 주입
    // @RequiredArgsConstructor와 함께 (롬복기능)
    // final을 붙여주면 AccountService 생성자의 필수 파라미터로 지정됨
    private final AccountRepository accountRepository;

    @Transactional      // createAccount()를 하나의 트랜잭션으로 묶어서 실행. 중간에 예외가 발생하면 롤백되어 모든 변경 사항이 취소됨
    public void createAccount() {
        Account account = Account.builder()
                .accountNumber("40000")
                .accountStatus(AccountStatus.IN_USER)
                .build();

        accountRepository.save(account);
    }

    @Transactional
    public Account getAccount(Long id) {
        if (id < 0) {
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();
    }
}
