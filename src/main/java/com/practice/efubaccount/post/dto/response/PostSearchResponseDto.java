package com.practice.efubaccount.post.dto.response;

import com.practice.efubaccount.post.domain.Post;

public record PostSearchResponseDto(
        // TODO 8-1) 게시글 검색 응답 DTO의 필드 정의하기
        Long postId,
        String title,
        String content,
        String writerNickname,
        Long viewCount
) {
    // TODO 8-2) Post 엔티티를 DTO로 변환하는 생성자 작성하기
    public PostSearchResponseDto(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getWriter().getNickname(),
                post.getViewCount()
        );
    }
}