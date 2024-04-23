package org.example.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // 엔티티의 생성, 수정일자를 자동으로 처리하기 위해 필요. 설정 클래스쪽에 추가해서 사용
public class JpaAuditingConfiguration {
}
