package com.creddit.credditmainserver.controller;

import com.creddit.credditmainserver.dto.request.MemberRequestDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.login.jwt.TokenDto;
import com.creddit.credditmainserver.login.jwt.TokenRequestDto;
import com.creddit.credditmainserver.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags={"회원가입/로그인"})
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value="회원가입")
    @PostMapping("/signup")
    public MemberResponseDto signup(@RequestBody MemberRequestDto memberRequestDto){
        return authService.signup(memberRequestDto);
    }

    @ApiOperation(value="로그인")
    @PostMapping("login")
    public TokenDto login(@RequestBody MemberRequestDto memberRequestDto){
        return authService.login(memberRequestDto);
    }

    @ApiOperation(value="accesstoken 재발급")
    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return authService.reissue(tokenRequestDto);
    }
}
