package org.example.account.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.account.type.TransactionResultType;
import org.example.account.type.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;

    @ManyToOne
    private Account account;
    private Long amount;
    private Long balanceSnapshot;

    private String transactionId;       // 보안 등의 이유로 id대신 transactionId를 사용해야함
    private LocalDateTime transactedAt;
}
