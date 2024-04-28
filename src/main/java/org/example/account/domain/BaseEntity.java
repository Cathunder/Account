package org.example.account.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "doesNotUseThisBuilder")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  // Auditing 기능을 사용하기 위해 필요 (@CreatedDate, @LastModifiedDate)
public class BaseEntity {

    @Id                 // PK로 설정
    @GeneratedValue     // PK값 자동으로 +1
    private Long id;

    @CreatedDate                // 시간을 자동으로 추가해줌
    private LocalDateTime createdAt;
    @LastModifiedDate           // 시간을 자동으로 추가해줌
    private LocalDateTime updatedAt;
}
