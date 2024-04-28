package org.example.account.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.account.exception.AccountException;
import org.example.account.type.AccountStatus;
import org.example.account.type.ErrorCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account extends BaseEntity {      // Account 테이블 생성

    @ManyToOne      // 다대일 관계 설정. (Account, Account, Account) - (AccountUser)
    private AccountUser accountUser;
    private String accountNumber;

    @Enumerated(EnumType.STRING)        // AccountStatus의 열거형 값들을 db에 저장할 때 숫자가 아닌 문자열 형태로 저장 "IN_USER", "UNREGISTERED"
    private AccountStatus accountStatus;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    public void useBalance(Long amount) {
        if (amount > balance) {
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }

        balance -= amount;
    }

    public void cancelBalance(Long amount) {
        if (amount < 0) {
            throw new AccountException(ErrorCode.INVALID_REQUEST);
        }

        balance += amount;
    }
}
