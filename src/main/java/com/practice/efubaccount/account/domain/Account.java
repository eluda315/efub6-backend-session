package com.practice.efubaccount.account.domain;

//import com.practice.efubaccount.comment.domain.Comment;

import jakarta.persistence.*;
import lombok.*;

//import java.util.ArrayList;
//import java.util.List;

@Entity
@Getter
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    // 회원 이메일
    @Column(unique = true)
    private String email;

    // 회원 비밀번호
    @Column(nullable = false)
    private String password;

    // 회원 닉네임
    @Column(nullable = false, updatable = false)
    private String nickname;

    // 회원 자기소개, default 값은 "안녕하세요!"
    @Column
    private String bio="안녕하세요!";

    // 회원 상태
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

//    // 연관관계의 Owner 설정
//    @OneToMany(mappedBy = "writer",cascade = CascadeType.ALL,orphanRemoval = true)
//    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Account(String email, String password, String nickname) {
        this.email=email;
        this.password=password;
        this.nickname=nickname;
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public void changeStatus(AccountStatus status) {
        this.status=status;
    }
}