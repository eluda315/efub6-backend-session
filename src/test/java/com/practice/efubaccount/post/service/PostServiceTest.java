package com.practice.efubaccount.post.service;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.account.service.AccountService;
import com.practice.efubaccount.post.domain.Post;
import com.practice.efubaccount.post.dto.response.PostResponse;
import com.practice.efubaccount.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostService postService;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = Account.builder()
                .email("efub@example.com")
                .password("password")
                .nickname("efub")
                .build();
    }

    @Test
    void PostService_생성_성공() {
        // TODO 2) 기본 stubbing
        given(accountService.findByAccountId(1L)).willReturn(testAccount);

        Account foundAccount = accountService.findByAccountId(1L);

        assertSame(testAccount, foundAccount);
    }

    @Test
    void deletePost_postRepository_delete에서_예외_전달() {
        // TODO 3) 의도적 실패 주입 테스트
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .writer(testAccount)
                .build();

        given(postRepository.findById(10L)).willReturn(Optional.of(post));
        given(accountService.findByAccountId(1L)).willReturn(testAccount);

        willThrow(new IllegalArgumentException("삭제 실패"))
                .given(postRepository).delete(any(Post.class));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postService.deletePost(10L, 1L));
        assertEquals("삭제 실패", exception.getMessage());

    }

    @Test
    void findAccountId_2번째호출에서_예외() {
        // given
        Account a1 = testAccount;
        Account a2 = Account.builder()
                .email("efub2@example.com")
                .password("testpw2")
                .nickname("efub2")
                .build();
        // TODO 4) 여러번 호출
        given(accountService.findByAccountId(any()))
                .willReturn(a1)
                .willThrow(new RuntimeException("두 번째 실패"))
                .willReturn(a2);

        // TODO 5) 검증
        assertSame(a1, accountService.findByAccountId(111L));
        assertThrows(RuntimeException.class, () -> accountService.findByAccountId(222L));
        assertSame(a2, accountService.findByAccountId(333L));


    }

    @Test
    void getPost_조회수증가_호출검증() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .writer(testAccount)
                .build();


        // TODO 6) Mock 객체 확인
        given(postRepository.findById(5L)).willReturn(Optional.of(post));

        PostResponse res = postService.getPost(5L);

        assertNotNull(res);
        verify(postRepository).increaseViewCount(5L);
        verify(postRepository, times(1)).findById(5L);
        verifyNoMoreInteractions(postRepository);
    }
}