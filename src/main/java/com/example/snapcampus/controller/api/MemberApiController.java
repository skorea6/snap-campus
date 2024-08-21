package com.example.snapcampus.controller.api;

import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.common.login.TokenInfo;
import com.example.snapcampus.dto.request.member.LoginDto;
import com.example.snapcampus.dto.request.member.TokenRefreshDto;
import com.example.snapcampus.dto.security.SnapPrincipal;
import com.example.snapcampus.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @Autowired
    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<TokenInfo> login(@RequestBody @Valid LoginDto loginDto) {
        TokenInfo tokenInfo = memberService.login(loginDto);
        return new BaseResponse<>(tokenInfo);
    }

    /**
     * Refresh 토큰을 이용한 Access 토큰 재발급
     */
    @PostMapping("/token/refresh")
    public BaseResponse<TokenInfo> tokenRefresh(@RequestBody @Valid TokenRefreshDto tokenRefreshDto) {
        TokenInfo tokenInfo = memberService.validateRefreshTokenAndCreateToken(tokenRefreshDto.getRefreshToken());
        return new BaseResponse<>(tokenInfo);
    }

    /**
     * 로그아웃
     */
    @GetMapping("/token/logout")
    public BaseResponse<Void> tokenLogout() {
        String userId = ((SnapPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        memberService.deleteAllRefreshToken(userId);
        return new BaseResponse<>("Logged out successfully");
    }
}

