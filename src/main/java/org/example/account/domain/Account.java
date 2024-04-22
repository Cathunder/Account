package org.example.account.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {      // Account 테이블 생성

    @Id     // PK로 설정
    @GeneratedValue
    private Long id;

    private String accountNumber;

    @Enumerated(EnumType.STRING)        // AccountStatus의 열거형 값들을 db에 저장할 때 문자열 형태로 저장 "IN_USER", "UNREGISTERED"
    private AccountStatus accountStatus;
}
