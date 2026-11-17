package com.practice.efubaccount.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class NewsletterBatchConfig {

//    [TODO 4] 조립 : Job -> Step -> Reader/Processor/Writer
//      Job 1개와 Step 2개 Bean을 만들고 서로 연결해 배치 작업을 완성합니다
//       - newsletterJob : (Job) 배치 작업 전체. clearStep → sendStep 순서로 실행합니다.
//       - clearStep     : (Step - Tasklet) 이전 발송 기록(mail_log)을 삭제합니다.
//       - sendStep      : (Step - Chunk)   Reader → Processor → Writer 순으로 100건씩 처리합니다.

    @Bean
    public Job newsletterJob(JobRepository jobRepository, Step clearStep, Step sendStep) {
        return new JobBuilder("newsletterJob", jobRepository)
                .start(clearStep)   // 첫 번째 step
                .next(sendStep)    // 두 번째 step
                .build();
    }

    @Bean
    public Step clearStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          JdbcTemplate jdbcTemplate) {
        return new StepBuilder("clearStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    jdbcTemplate.update("DELETE FROM mail_log"); // 이전 발송 기록 삭제
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step sendStep(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         JdbcPagingItemReader<Subscriber> subscriberReader,
                         ItemProcessor<Subscriber, MailLog> newsletterProcessor,
                         JdbcBatchItemWriter<MailLog> mailLogWriter) {
        return new StepBuilder("sendStep", jobRepository)
                .<Subscriber, MailLog>chunk(100, transactionManager) // 몇 건씩 묶어 처리할지
                .reader(subscriberReader)
                .processor(newsletterProcessor)
                .writer(mailLogWriter)
                .build();
    }


//    [TODO 1] Reader (subscriberReader)
//      accounts 테이블에서 발송 대상을 100건씩 읽어옵니다

//    완성되어야 하는 SQL:
//   SELECT account_id, email, nickname
//   FROM accounts
//   WHERE status = 'ACTIVE' AND email LIKE 'newsletter-user-%'
//   ORDER BY account_id ASC
//   LIMIT 100
    @Bean
    public JdbcPagingItemReader<Subscriber> subscriberReader(DataSource dataSource) {
        return new JdbcPagingItemReaderBuilder<Subscriber>()
                .name("subscriberReader")
                .dataSource(dataSource)
                .selectClause("account_id, email, nickname")
                .fromClause("accounts")
                .whereClause("status = 'ACTIVE' AND email LIKE 'newsletter-user-%'")
                .sortKeys(Map.of("account_id", Order.ASCENDING))
                .pageSize(100)
                .rowMapper(new DataClassRowMapper<>(Subscriber.class))
                .build();
    }

//    [TODO 2] Processor (newsletterProcessor)
//      Reader가 읽은 Subscriber 1명을 받아서 -> Writer가 저장할 MailLog 1건으로 바꿔줍니다
//      (입력: Subscriber → 출력: MailLog)

    @Bean
    public ItemProcessor<Subscriber, MailLog> newsletterProcessor() {
        return subscriber -> {
            // 💥 메일 서버 장애 시뮬레이션
//             if (subscriber.nickname().equals("구독자557")) {
//                 throw new IllegalStateException("💥 메일 서버 장애 발생");
//             }

            String subject = "[EFUB] " + subscriber.nickname() + "님, 이번 주 소식이 도착했어요!";
            return MailLog.of(subscriber, subject);
        };
    }

//    [TODO 3] Writer (mailLogWriter)
//      Processor가 만든 MailLog를 mail_log 테이블에 INSERT 합니다
    @Bean
    public JdbcBatchItemWriter<MailLog> mailLogWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MailLog>()
                .dataSource(dataSource)
                .sql("INSERT INTO mail_log (account_id, email, subject, sent_at) "
                     + "VALUES (:accountId, :email, :subject, :sentAt)")
                .beanMapped()
                .build();
    }
}