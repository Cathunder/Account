package org.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // enum 매핑 참고하기
    USER_NOT_FOUND("사용자가 없습니다.");

    private final String description;
}
