package com.practice.efubaccount.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

/**
 * seedLoadTestData 작업에서만 활성화되는 데이터 준비 Runner.
 * 10,000건이 이미 있으면 건너뛰며, 일반 서버 실행에서는 동작하지 않는다.
 */
// @Slf4j
// @Component
// @RequiredArgsConstructor
// @Order(1)
// @ConditionalOnProperty(name = "loadtest.seed.enabled", havingValue = "true")
// public class LoadTestDataSeeder implements ApplicationRunner {

//     private static final String LOAD_TEST_EMAIL = "load-test@efub.dev";
//     private static final String LOAD_TEST_TITLE_PREFIX = "[LOAD-TEST] ";
//     private static final int POST_COUNT = 10_000;
//     private static final int BATCH_SIZE = 1_000;
//     private static final LocalDateTime BASE_TIME = LocalDateTime.of(2026, 1, 1, 0, 0);

//     private final JdbcTemplate jdbcTemplate;

//     @Override
//     @Transactional
//     public void run(ApplicationArguments args) {
//         Long accountId = findOrCreateLoadTestAccount();
//         Integer existingCount = jdbcTemplate.queryForObject(
//                 """
//                 SELECT COUNT(*)
//                 FROM post
//                 WHERE writer_account_id = ?
//                   AND title LIKE '[LOAD-TEST] %'
//                 """,
//                 Integer.class,
//                 accountId
//         );

//         if (existingCount != null && existingCount == POST_COUNT) {
//             log.info("Load-test data is already ready: {} posts", POST_COUNT);
//             return;
//         }

//         jdbcTemplate.update(
//                 "DELETE FROM post WHERE writer_account_id = ? AND title LIKE '[LOAD-TEST] %'",
//                 accountId
//         );

//         List<Integer> sequenceNumbers = IntStream.rangeClosed(1, POST_COUNT)
//                 .boxed()
//                 .toList();

//         jdbcTemplate.batchUpdate(
//                 """
//                 INSERT INTO post (
//                     writer_account_id, title, content, view_count, created_at, modified_at
//                 ) VALUES (?, ?, ?, 0, ?, ?)
//                 """,
//                 sequenceNumbers,
//                 BATCH_SIZE,
//                 (statement, sequence) -> {
//                     Timestamp createdAt = Timestamp.valueOf(BASE_TIME.plusSeconds(sequence));
//                     statement.setLong(1, accountId);
//                     statement.setString(2, LOAD_TEST_TITLE_PREFIX + "게시글 " + sequence);
//                     statement.setString(3, "페이지네이션 성능 비교를 위한 게시글 본문입니다. 번호: " + sequence);
//                     statement.setTimestamp(4, createdAt);
//                     statement.setTimestamp(5, createdAt);
//                 }
//         );

//         log.info("Created {} load-test posts in MySQL", POST_COUNT);
//     }

//     private Long findOrCreateLoadTestAccount() {
//         List<Long> accountIds = jdbcTemplate.query(
//                 "SELECT account_id FROM accounts WHERE email = ?",
//                 (resultSet, rowNumber) -> resultSet.getLong("account_id"),
//                 LOAD_TEST_EMAIL
//         );

//         if (!accountIds.isEmpty()) {
//             return accountIds.get(0);
//         }

//         jdbcTemplate.update(
//                 """
//                 INSERT INTO accounts (email, password, nickname, bio, status)
//                 VALUES (?, 'not-used', 'load-tester', '부하 테스트 계정', 'ACTIVE')
//                 """,
//                 LOAD_TEST_EMAIL
//         );

//         return jdbcTemplate.queryForObject(
//                 "SELECT account_id FROM accounts WHERE email = ?",
//                 Long.class,
//                 LOAD_TEST_EMAIL
//         );
//     }
// }
