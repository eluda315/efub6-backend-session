package com.practice.efubaccount.account.repository;

import com.practice.efubaccount.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account, Long> {

    // 이메일 중복검사를 위한 쿼리
    boolean existsByEmail(String email);

    // 회원 ID로 조회
    Optional<Account> findByAccountId(Long accountId);
}
