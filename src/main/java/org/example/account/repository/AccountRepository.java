package org.example.account.repository;

import org.example.account.domain.Account;
import org.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // 스프링 데이터 JPA
    // extends JpaRepository<엔티티의 이름, PK의 타입>
    // 이 interface는 Account 테이블(엔티티)에 접속하기 위한 interface

    Optional<Account> findFirstByOrderByIdDesc(); // 메서드명대로 쿼리를 알아서 생성해줌

    // Account에 AccountUser랑 다대일 관계를 가지고 있기 때문에 계좌 개수 구할 수 있음
    Integer countByAccountUser(AccountUser accountUser);

    Optional<Account> findByAccountNumber(String accountNumber);
}
