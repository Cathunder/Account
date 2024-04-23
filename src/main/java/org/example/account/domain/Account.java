package org.example.account.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.account.type.AccountStatus;
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
@EntityListeners(AuditingEntityListener.class)      // Auditing 기능을 사용하기 위해 필요 (@CreatedDate, @LastModifiedDate)
public class Account {      // Account 테이블 생성
    @Id                 // PK로 설정
    @GeneratedValue     // PK값 자동으로 +1
    private Long id;

    @ManyToOne      // 다대일 관계 설정. (Account, Account, Account) - (AccountUser)
    private AccountUser accountUser;
    private String accountNumber;

    @Enumerated(EnumType.STRING)        // AccountStatus의 열거형 값들을 db에 저장할 때 숫자가 아닌 문자열 형태로 저장 "IN_USER", "UNREGISTERED"
    private AccountStatus accountStatus;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    @CreatedDate                // 시간을 자동으로 추가해줌
    private LocalDateTime createdAt;
    @LastModifiedDate           // 시간을 자동으로 추가해줌
    private LocalDateTime updatedAt;
}
