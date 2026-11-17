package com.practice.efubaccount.batch;

public record Subscriber(
        Long accountId,
        String email,
        String nickname
) {
}
