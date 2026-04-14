//package com.practice.efubaccount.comment.domain;
//
//import com.practice.efubaccount.account.domain.Account;
////import com.practice.efubaccount.global.domain.BaseEntity;
//import com.practice.efubaccount.post.domain.Post;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Comment {
//
//    @Id
//    @Column(name="comment_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(length=1000)
//    private String content;
//
//    // 연관관계
//
//
//    @Builder
//    public Comment(String content, Account writer, Post post) {
//        this.content = content;
//        this.writer = writer;
//        this.post = post;
//    }
//}