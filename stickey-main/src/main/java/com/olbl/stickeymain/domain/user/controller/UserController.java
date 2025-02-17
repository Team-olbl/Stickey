package com.olbl.stickeymain.domain.user.controller;

import static com.olbl.stickeymain.global.result.ResultCode.CHECK_EMAIL_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.GET_PROFILE_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.GET_USER_INFO_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.MODIFY_PREFERENCE_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.REGIST_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.SEND_EMAIL_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.TOKEN_REISSUE_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.UPDATE_USER_INFO_SUCCESS;
import static com.olbl.stickeymain.global.result.ResultCode.UPDATE_USER_PASSWORD_SUCCESS;

import com.olbl.stickeymain.domain.user.dto.EmailCheckReq;
import com.olbl.stickeymain.domain.user.dto.EmailCodeReq;
import com.olbl.stickeymain.domain.user.dto.PreferenceReq;
import com.olbl.stickeymain.domain.user.dto.ProfileRes;
import com.olbl.stickeymain.domain.user.dto.SignUpReq;
import com.olbl.stickeymain.domain.user.dto.UpdatePasswordReq;
import com.olbl.stickeymain.domain.user.dto.UserInfoReq;
import com.olbl.stickeymain.domain.user.dto.UserInfoRes;
import com.olbl.stickeymain.domain.user.organization.dto.OrganSignUpReq;
import com.olbl.stickeymain.domain.user.organization.service.OrganizationService;
import com.olbl.stickeymain.domain.user.service.MailService;
import com.olbl.stickeymain.domain.user.service.UserService;
import com.olbl.stickeymain.global.jwt.JWTUtil;
import com.olbl.stickeymain.global.result.ResultResponse;
import com.olbl.stickeymain.global.result.error.ErrorCode;
import com.olbl.stickeymain.global.result.error.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final MailService mailService;
    private final OrganizationService organizationService;

    @Operation(summary = "회원가입")
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> signup(
        @RequestPart(value = "signUpReq") SignUpReq signUpReq,
        @RequestPart(value = "profile") MultipartFile profile) {
        userService.signup(signUpReq, profile);
        return ResponseEntity.ok(ResultResponse.of(REGIST_SUCCESS));
    }

    @Operation(summary = "이메일 인증 코드 발송")
    @PostMapping("/auth")
    public ResponseEntity<ResultResponse> sendAuthEmail(
        @RequestBody @Valid EmailCodeReq emailCodeReq) {
        mailService.sendAuthEmail(emailCodeReq);
        return ResponseEntity.ok(ResultResponse.of(SEND_EMAIL_SUCCESS));
    }

    @Operation(summary = "이메일 인증 코드 확인")
    @PostMapping("/auth-check")
    public ResponseEntity<ResultResponse> checkAuthEmail(
        @RequestBody @Valid EmailCheckReq emailCheckReq) {
        boolean state = userService.checkAuthEmail(emailCheckReq);
        return ResponseEntity.ok(ResultResponse.of(CHECK_EMAIL_SUCCESS, state));
    }

    @Operation(summary = "이메일로 임시 비밀번호 발송")
    @PatchMapping
    public ResponseEntity<ResultResponse> findPassword(@RequestBody EmailCodeReq emailCodeReq) {
        String newPassword = userService.findPassword(emailCodeReq); // 임시 비밀번호 발급
        mailService.sendPasswordEmail(emailCodeReq.getEmail(), newPassword); // 이메일 발송
        return ResponseEntity.ok(ResultResponse.of(SEND_EMAIL_SUCCESS));
    }

    @Operation(summary = "단체 회원가입")
    @PostMapping(value = "/signup/organization", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> signupOrganization(
        @RequestPart(value = "organSignUpReq") OrganSignUpReq organSignUpReq,
        @RequestPart(value = "profile") MultipartFile profile,
        @RequestPart(value = "registrationFile") MultipartFile registrationFile) {
        organizationService.signup(organSignUpReq, profile, registrationFile);
        return ResponseEntity.ok(ResultResponse.of(REGIST_SUCCESS));
    }

    @Operation(summary = "회원 프로필 조회 (본인이)")
    @GetMapping("/profiles")
    public ResponseEntity<ResultResponse> getProfile(Authentication authentication) {
        ProfileRes profile = userService.getProfile(authentication);
        return ResponseEntity.ok(ResultResponse.of(GET_PROFILE_SUCCESS, profile));
    }

    @Operation(summary = "개인 유저 정보 조회 (수정 시)")
    @GetMapping("/profile/info")
    public ResponseEntity<ResultResponse> getUserInfo() {
        UserInfoRes userInfo = userService.getUserInfo();
        return ResponseEntity.ok(ResultResponse.of(GET_USER_INFO_SUCCESS, userInfo));
    }

    @Operation(summary = "개인 유저 정보 수정")
    @PatchMapping(value = "/profile/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> updateUserInfo(@RequestPart UserInfoReq userInfoReq,
        @RequestPart(required = false) MultipartFile profile) {
        userService.updateUserInfo(userInfoReq, profile);
        return ResponseEntity.ok(ResultResponse.of(UPDATE_USER_INFO_SUCCESS));
    }

    @Operation(summary = "개인 유저 비밀번호 수정")
    @PatchMapping(value = "/profile/info/password")
    public ResponseEntity<ResultResponse> updateUserPassword(
        @RequestBody UpdatePasswordReq updatePasswordReq) {
        userService.updateUserPassword(updatePasswordReq);
        return ResponseEntity.ok(ResultResponse.of(UPDATE_USER_PASSWORD_SUCCESS));
    }

    @Operation(summary = "내 선호 구단 수정")
    @PatchMapping("/profile/preference")
    public ResponseEntity<ResultResponse> modifyPreference(
        @RequestBody PreferenceReq preferenceReq, Authentication authentication) {
        userService.modifyPreference(preferenceReq, authentication);
        return ResponseEntity.ok(ResultResponse.of(MODIFY_PREFERENCE_SUCCESS));
    }

    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<ResultResponse> reissueToken(HttpServletRequest request,
        HttpServletResponse response) {

        // 리프레시 토큰 획득
        String token = request.getHeader("Authorization");

        // 리프레시 토큰이 없거나, 만료되었다면 예외 발생
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_AVAILABLE);
        }

        String refreshToken = token.split(" ")[1];

        // 리프레시 토큰 유효성 검사 - 토큰 자체의 유효성 검사
        if (!jwtUtil.validateToken(refreshToken, "refresh")) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_AVAILABLE);
        }

        // Username, Role 정보 획득
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 유효한 리프레시 토큰인지 검사  - 해당 유저의 토큰이 맞는지 검사
        String accurateRefresh = jwtUtil.getRefreshEntity(username);
        if (accurateRefresh == null || !accurateRefresh.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_AVAILABLE);
        }

        // 새로운 Access Token, Refresh Token 만들고, Response에 추가
        String newAccess = jwtUtil.createJWT("access", username, role, 10800000L);
        String newRefresh = jwtUtil.createJWT("refresh", username, role, 259200000L);

        response.setHeader("access", newAccess);
        response.setHeader("refresh", newRefresh);

        // 기존 Refresh Token은 삭제하고 새 Refresh Token 업데이트
        jwtUtil.addRefreshEntity(username, newRefresh);
        return ResponseEntity.ok(ResultResponse.of(TOKEN_REISSUE_SUCCESS));
    }
}
