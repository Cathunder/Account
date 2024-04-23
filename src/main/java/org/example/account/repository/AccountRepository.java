package org.example.account.repository;

import org.example.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // 스프링 데이터 JPA
    // extends JpaRepository<엔티티의 이름, PK의 타입>
    // Account 테이블(엔티티)에 접속하기 위한 interface

    Optional<Account> findFirstByOrderByIdDesc(); // 메서드명대로 쿼리를 알아서 생성함
}
