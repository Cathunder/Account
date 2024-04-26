package org.example.account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.account.domain.Account;
import org.example.account.domain.AccountUser;
import org.example.account.domain.Transaction;
import org.example.account.dto.TransactionDto;
import org.example.account.exception.AccountException;
import org.example.account.repository.AccountRepository;
import org.example.account.repository.AccountUserRepository;
import org.example.account.repository.TransactionRepository;
import org.example.account.type.AccountStatus;
import org.example.account.type.ErrorCode;
import org.example.account.type.TransactionResultType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.example.account.type.TransactionResultType.F;
import static org.example.account.type.TransactionResultType.S;
import static org.example.account.type.TransactionType.USE;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;

    /**
     * 사용자 없는 경우
     * 사용자 아이디와 계좌 소유주가 다른 경우
     * 계좌가 이미 해지상태인 경우
     * 거래금액이 잔액보다 큰 경우
     * 거래금액이 너무 작거나 큰 경우 (UseBalance의 @Valid로 이미 적용)
     */
    @Transactional
    public TransactionDto useBalance(Long userId, String accountNumber,
                                     Long amount) {
        AccountUser user = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateUseBalance(user, account, amount);

//        Long accountBalance = account.getBalance();
//        account.setBalance(accountBalance - amount);
//        위처럼 코드를 작성하는 것보다 엔티티에 따로 메서드를 만들어 주는것이 안전하다.
        account.useBalance(amount);

        return TransactionDto.fromEntity(saveAndGetTransaction(S, account, amount));
    }

    private void validateUseBalance(AccountUser user, Account account, Long amount) {
        if (!Objects.equals(user.getId(), account.getAccountUser().getId())) {
            throw new AccountException(ErrorCode.USER_ACCOUNT_UN_MATCH);
        }

        if (account.getAccountStatus() != AccountStatus.IN_USER) {
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }

        if (account.getBalance() < amount) {
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
    }

    @Transactional
    public void saveFailedUseTransaction(String accountNumber, Long amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        saveAndGetTransaction(F, account, amount);
    }

    private Transaction saveAndGetTransaction(
            TransactionResultType transactionResultType,
            Account account,
            Long amount) {
        return transactionRepository.save(
                Transaction.builder()
                        .transactionType(USE)
                        .transactionResultType(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balanceSnapshot(account.getBalance())
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
    }
}
