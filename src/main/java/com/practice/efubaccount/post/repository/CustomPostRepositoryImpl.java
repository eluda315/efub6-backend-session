package com.practice.efubaccount.post.repository;

import com.practice.efubaccount.account.domain.QAccount;
import com.practice.efubaccount.post.domain.Post;
import com.practice.efubaccount.post.domain.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> search(String keyword, String writerNickname) {

        // TODO 4-1) Q클래스 객체 선언하기

        // TODO 4-2) BooleanBuilder를 사용해 동적 검색 조건 작성하기

        // TODO 4-3) QueryDSL로 게시글 조회 쿼리 작성하기

        return List.of();
    }
}