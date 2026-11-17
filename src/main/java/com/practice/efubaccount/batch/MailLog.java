package com.practice.efubaccount.batch;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mail_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    private MailLog(Long accountId, String email, String subject) {
        this.accountId = accountId;
        this.email = email;
        this.subject = subject;
        this.sentAt = LocalDateTime.now();
    }

    public static MailLog of(Subscriber subscriber, String subject) {
        return new MailLog(subscriber.accountId(), subscriber.email(), subject);
    }
}