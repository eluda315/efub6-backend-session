package com.practice.efubaccount.post;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.repository.AccountRepository;
import com.practice.efubaccount.post.domain.Post;
import com.practice.efubaccount.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class QueryDslTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PostRepository postRepository;

    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        // TODO 9-1) 테스트용 계정 및 게시글 데이터 생성하기
    }

    @Test
    void searchByKeyword() {
        // TODO 9-2) 키워드로 게시글 검색 테스트하기
        throw new UnsupportedOperationException("TODO 9-2");
    }

    @Test
    void searchByWriterNickname() {
        // TODO 9-3) 작성자 닉네임으로 게시글 검색 테스트하기
        throw new UnsupportedOperationException("TODO 9-3");
    }

    @Test
    void searchByKeywordAndWriter() {
        // TODO 9-4) 키워드와 작성자 조건을 함께 적용해 검색 테스트하기
        throw new UnsupportedOperationException("TODO 9-4");

    }
}