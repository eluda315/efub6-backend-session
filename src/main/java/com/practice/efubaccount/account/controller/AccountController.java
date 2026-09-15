package com.practice.efubaccount.account.controller;

import com.practice.efubaccount.account.dto.request.BioUpdateRequestDto;
import com.practice.efubaccount.account.dto.request.CreateAccountRequestDto;
import com.practice.efubaccount.account.dto.response.AccountResponseDto;
import com.practice.efubaccount.account.dto.response.CreateAccountResponseDto;
import com.practice.efubaccount.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 회원 조회: GET /accounts/{accountId}
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable("accountId") Long accountId) {
        AccountResponseDto responseDto = accountService.getAccount(accountId);
        return ResponseEntity.ok(responseDto);
    }

    //TODO: Redis 조회
    @GetMapping("/redis/{accountId}")
    public String getEmailByIdFromRedis(@PathVariable("accountId") Long accountId) {
        return accountService.findEmailByIdFromRedis(accountId);
    }

    //TODO: Mongo DB 조회
    @GetMapping("/mongodb/{accountId}")
    public String getNicknameByIdFromMongodb(@PathVariable("accountId") Long accountId) {
        return accountService.findNicknameByIdFromMongo(accountId);
    }


    // 계정 생성 POST /accounts
    @PostMapping
    public ResponseEntity<CreateAccountResponseDto> createAccount(@RequestBody CreateAccountRequestDto requestDto) {
        CreateAccountResponseDto responseDto = accountService.createAccount(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 계정 프로필(자기소개) 수정: PATCH /accounts/profile/{accountId}
    @PatchMapping("/profile/{accountId}")
    public ResponseEntity<AccountResponseDto> updateAccount(@PathVariable("accountId") Long accountId,
                                                            @RequestBody BioUpdateRequestDto requestDto) {
        AccountResponseDto responseDto = accountService.updateAccount(accountId, requestDto);
        return ResponseEntity.ok(responseDto);
    }


    // 계정 논리적 삭제(탈퇴): PATCH /accounts/{accountId}
    @PatchMapping("/{accountId}")
    public ResponseEntity<Map<String, String>> deleteAccount(@PathVariable("accountId") Long accountId) {
        accountService.deleteAccount(accountId);  // 상태 변경만 수행
        Map<String, String> response = new HashMap<>();
        response.put("message", "성공적으로 탈퇴되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 계정 물리적 삭제: DELETE /accounts/{accountId}
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Map<String, String>> physicalDeleteAccount(@PathVariable("accountId") Long accountId) {
        accountService.physicalDeleteAccount(accountId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "성공적으로 탈퇴되었습니다.");
        return ResponseEntity.ok(response);
    }
}