package org.example.account.repository;

import org.example.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {   // Account 테이블에 접속하기 위한 interface

}
