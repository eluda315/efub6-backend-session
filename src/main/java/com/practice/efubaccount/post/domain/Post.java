package com.practice.efubaccount.post.domain;

// week4 import 문 추가 for 연관관계 매핑
import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.comment.domain.Comment;

import com.practice.efubaccount.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// week4 import 문 추가
import java.util.ArrayList;
import java.util.List;

@Entity
// 최신 게시글 정렬을 보조하는 복합 인덱스를 적용한다.
// 기존 DB에 이미 이 인덱스가 있으면 주석 처리만으로 제거되지는 않는다.
@Table(indexes = {
        @Index(name = "idx_post_created_at_id", columnList = "created_at, id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long viewCount;

    // week4 연관관계의 Owner 설정
    // post랑 comment의 관계에서 누가 주인인지 정하고 부모 (post)가 자식 (comment)의 생명 주기를 관리하도록 설정한 코드
    @OneToMany(mappedBy="post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Post(String title, Account writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.viewCount = 0L;
    }

    public void changeContent(String newContent) {
        this.content = newContent;
    }
}
