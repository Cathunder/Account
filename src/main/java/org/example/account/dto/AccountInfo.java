package org.example.account.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfo {      // 해당 역할만 하는 dto들을 만들어서 사용하는 것이 좋다.
    private String accountNumber;
    private Long balance;
}
