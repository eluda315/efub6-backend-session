package com.practice.efubaccount.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_view_counter_shard")
@IdClass(PostViewCounterShardId.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostViewCounterShard {

    @Id
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Id
    @Column(name = "shard_id", nullable = false)
    private Integer shardId;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;
}
