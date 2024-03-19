package com.olbl.stickeymain.domain.user.controller;

import static com.olbl.stickeymain.global.result.ResultCode.REGIST_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.SEND_EMAIL_SUCCESS;

import com.olbl.stickeymain.domain.user.dto.EmailCodeReq;
import com.olbl.stickeymain.domain.user.dto.SignUpReq;
import com.olbl.stickeymain.domain.user.service.MailService;
import com.olbl.stickeymain.domain.user.service.UserService;
import com.olbl.stickeymain.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "users", description = "회원 API")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ResultResponse> signup(
        @RequestPart(value = "signUpReq") SignUpReq signUpReq,
        @RequestPart(value = "profile") MultipartFile profile) {
        userService.signup(signUpReq, profile);
        return ResponseEntity.ok(ResultResponse.of(REGIST_SUCCESS));
    }

    @Operation(summary = "이메일 인증 코드 발송")
    @PostMapping("/auth")
    public ResponseEntity<ResultResponse> sendAuthEmail(@RequestBody EmailCodeReq emailCodeReq) {
        mailService.sendAuthEmail(emailCodeReq);
        return ResponseEntity.ok(ResultResponse.of(SEND_EMAIL_SUCCESS));
    }

}
