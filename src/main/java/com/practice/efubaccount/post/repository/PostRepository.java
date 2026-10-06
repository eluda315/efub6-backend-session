package com.practice.efubaccount.post.repository;

import com.practice.efubaccount.post.domain.Post;
import com.practice.efubaccount.post.dto.summary.PostSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // @Query와 findPostSummaries 메서드
    // 게시글 목록에 필요한 4개 필드만 조회하는 Page DTO projection을 작성한다.
    // Pageable은 Service에서 전달하고, 전체 개수는 별도 countQuery로 조회한다.
    @Query(
            value = """
                    SELECT new com.practice.efubaccount.post.dto.summary.PostSummary(
                        p.id, p.writer.nickname, p.title, p.viewCount
                    )
                    FROM Post p
                    """,
            countQuery = "SELECT COUNT(p) FROM Post p"
    )
    Page<PostSummary> findPostSummaries(Pageable pageable);

    Optional<Post> findById(Long id);

    // 생성한 날짜 기준 전체 조회
    List<Post> findAllByOrderByCreatedAtDesc();

    // 조회수 증가
     @Modifying(clearAutomatically = true)
     @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
     int increaseViewCount(@Param("postId") Long postId);
}
