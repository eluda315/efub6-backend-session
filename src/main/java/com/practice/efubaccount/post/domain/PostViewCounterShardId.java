package com.practice.efubaccount.post.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PostViewCounterShardId implements Serializable {
    private Long postId;
    private Integer shardId;
}
