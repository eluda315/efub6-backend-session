package com.practice.efubaccount.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
@ConditionalOnProperty(name = "newsletter.seed.enabled", havingValue = "true")
public class NewsletterDataSeeder implements ApplicationRunner {

    private static final String EMAIL_PATTERN = "newsletter-user-%@efub.dev";
    private static final int TOTAL_COUNT = 1_000;
    private static final int ACTIVE_COUNT = 900;
    private static final int BATCH_SIZE = 1_000;

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM accounts WHERE email LIKE ?", Integer.class, EMAIL_PATTERN);
        Integer active = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM accounts WHERE email LIKE ? AND status = 'ACTIVE'", Integer.class, EMAIL_PATTERN);

        if (total != null && total == TOTAL_COUNT && active != null && active == ACTIVE_COUNT) {
            log.info("Newsletter data is already ready: {} accounts ({} ACTIVE)", TOTAL_COUNT, ACTIVE_COUNT);
            return;
        }

        jdbcTemplate.update("DELETE FROM accounts WHERE email LIKE ?", EMAIL_PATTERN);

        List<Integer> numbers = IntStream.rangeClosed(1, TOTAL_COUNT).boxed().toList();

        jdbcTemplate.batchUpdate(
                """
                INSERT INTO accounts (email, password, nickname, bio, status)
                VALUES (?, 'not-used', ?, '뉴스레터 배치 실습용 계정', ?)
                """,
                numbers,
                BATCH_SIZE,
                (statement, number) -> {
                    statement.setString(1, "newsletter-user-" + number + "@efub.dev");
                    statement.setString(2, "구독자" + number);
                    statement.setString(3, number % 10 == 0 ? "DEACTIVATED" : "ACTIVE");
                }
        );

        log.info("Created {} newsletter accounts ({} ACTIVE)", TOTAL_COUNT, ACTIVE_COUNT);
    }
}
