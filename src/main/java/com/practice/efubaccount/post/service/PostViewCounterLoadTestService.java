package com.practice.efubaccount.post.service;

import com.practice.efubaccount.global.exception.CustomException;
import com.practice.efubaccount.global.exception.ErrorCode;
import com.practice.efubaccount.post.repository.PostRepository;
import com.practice.efubaccount.post.repository.PostViewCounterShardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ThreadLocalRandom;

 @Service
 @RequiredArgsConstructor
 public class PostViewCounterLoadTestService {

     private static final int SHARD_COUNT = 16;

     private final PostRepository postRepository;
     private final PostViewCounterShardRepository shardRepository;

     @Transactional
     public void increaseSingleCounter(Long postId) {
         if (postRepository.increaseViewCount(postId) == 0) {
             throw new CustomException(ErrorCode.POST_NOT_FOUND);
         }
     }

     @Transactional
     public void increaseShardedCounter(Long postId) {
         int shardId = ThreadLocalRandom.current().nextInt(SHARD_COUNT);
         shardRepository.increaseViewCount(postId, shardId);
     }
 }
