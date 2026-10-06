package com.practice.efubaccount.post.repository;

import com.practice.efubaccount.post.domain.PostViewCounterShard;
import com.practice.efubaccount.post.domain.PostViewCounterShardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

 public interface PostViewCounterShardRepository
         extends JpaRepository<PostViewCounterShard, PostViewCounterShardId> {

      // (post_id, shard_id)를 키로 최초 INSERT 또는 원자적 +1을 구현한다.
      // MySQL native UPSERT를 사용한다. 단일 counter와 쿼리 비용도 다름을 비교한다.
      @Modifying
      @Query(value = """
              INSERT INTO post_view_counter_shard (post_id, shard_id, view_count)
              VALUES (:postId, :shardId, 1)
              ON DUPLICATE KEY UPDATE view_count = view_count + 1
              """, nativeQuery = true)
      int increaseViewCount(
              @Param("postId") Long postId,
              @Param("shardId") int shardId
      );
  }
