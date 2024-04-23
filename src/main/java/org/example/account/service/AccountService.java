package org.example.account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.account.domain.Account;
import org.example.account.domain.AccountUser;
import org.example.account.exception.AccountException;
import org.example.account.repository.AccountRepository;
import org.example.account.repository.AccountUserRepository;
import org.example.account.type.AccountStatus;
import org.example.account.type.ErrorCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.example.account.type.AccountStatus.IN_USER;

@Service
@RequiredArgsConstructor
public class AccountService {

    // 생성자 주입
    // @RequiredArgsConstructor와 함께 (롬복기능)
    // final을 붙여주면 AccountService 생성자의 필수 파라미터로 지정됨
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    /**
     * 사용자가 있는지 조회
     * 계좌번호 생성
     * 계좌를 저장하고
     * 그 정보를 넘긴다.
     */
    @Transactional      // createAccount()를 하나의 트랜잭션으로 묶어서 실행. 중간에 예외가 발생하면 롤백되어 모든 변경 사항이 취소됨
    public Account createAccount(Long userId, Long initialBalance) {

        // orElseThrow()
        // 해당 키에 맞는 엔티티가 존재하면 Optional로 감싼 엔티티 반환
        // 그렇지 않으면 Optional 반환
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));

        // map 메서드를 사용하면 Optional 객체를 원하는 형대로 변환 가능
        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> Integer.parseInt(account.getAccountNumber()) + 1 + "")
                .orElse("1000000000");

        return accountRepository.save(
                Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(IN_USER)
                        .accountNumber(newAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build()
        );
    }

    @Transactional
    public Account getAccount(Long id) {
        if (id < 0) {
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();
    }
}
