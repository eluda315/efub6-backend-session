package com.practice.efubaccount.post.controller;

import com.practice.efubaccount.post.dto.request.PostCreateRequest;
import com.practice.efubaccount.post.dto.request.PostUpdateRequest;
import com.practice.efubaccount.post.dto.response.PostListResponse;
import com.practice.efubaccount.post.dto.response.PostPageResponse;
import com.practice.efubaccount.post.dto.response.PostResponse;
import com.practice.efubaccount.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시물 생성
    @PostMapping
    public ResponseEntity<Void> createPost(@Valid @RequestBody PostCreateRequest request) {
        Long id = postService.createPost(request);
        return ResponseEntity.created(URI.create("/posts/"+id)).build();
    }

    // 페이지네이션이 적용된 게시물 조회
    @GetMapping
    public ResponseEntity<PostPageResponse> getPosts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(postService.getPosts(page, size));
    }

    // 성능 비교 실습용: 모든 게시물을 한 번에 조회
    @GetMapping("/all")
    public ResponseEntity<PostListResponse> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 게시물 1개 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id){
        return ResponseEntity.ok(postService.getPost(id));
    }

    // 게시글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePostContent(@PathVariable("id") Long postId,
                                                  @RequestHeader("Auth-Id") Long accountId,
                                                  @Valid @RequestBody PostUpdateRequest request) {
        postService.updatePostContent(postId, request, accountId);
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long postId,
                                           @RequestHeader("Auth-Id") Long accountId){
        postService.deletePost(postId, accountId);
        return ResponseEntity.noContent().build();
    }
}
