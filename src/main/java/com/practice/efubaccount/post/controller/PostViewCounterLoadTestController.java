package com.practice.efubaccount.post.controller;

import com.practice.efubaccount.post.service.PostViewCounterLoadTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/load-test/posts")
@RequiredArgsConstructor
public class PostViewCounterLoadTestController {

    private final PostViewCounterLoadTestService postViewCounterService;

    @GetMapping("/{postId}/views/single-counter")
    public ResponseEntity<Void> increaseSingleCounter(@PathVariable Long postId) {
        postViewCounterService.increaseSingleCounter(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}/views/sharded-counter")
    public ResponseEntity<Void> increaseShardedCounter(@PathVariable Long postId) {
        postViewCounterService.increaseShardedCounter(postId);
        return ResponseEntity.noContent().build();
    }
}
