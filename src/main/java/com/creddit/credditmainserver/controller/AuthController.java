package com.creddit.credditmainserver.controller;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.request.MemberRequestDto;
import com.creddit.credditmainserver.dto.request.SocialLoginRequestDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.login.jwt.TokenDto;
import com.creddit.credditmainserver.login.jwt.TokenRequestDto;
import com.creddit.credditmainserver.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags={"회원가입/로그인"})
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value="소셜로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "회원의 email"),
            @ApiImplicitParam(name = "nickname", value = "회원의 nickname"),
            @ApiImplicitParam(name = "type", value = "로그인 타입(kakao 혹은 naver)")
    })
    @PostMapping("/login/social")
    public TokenDto socialLogin(@RequestBody SocialLoginRequestDto socialLoginRequestDto) throws Exception {
        return authService.socialLogin(socialLoginRequestDto);
    }


    @ApiOperation(value="회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody MemberRequestDto memberRequestDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authService.signup(memberRequestDto),HttpStatus.OK);
    }

    @ApiOperation(value="로그인")
    @PostMapping("login")
    public TokenDto login(@RequestBody MemberRequestDto memberRequestDto) {
        return authService.login(memberRequestDto);
    }


    @PostMapping("/reissueAccessToken")
    public TokenDto  reissueAccessToken(@RequestBody TokenRequestDto tokenRequestDto){
        return authService.reissueAccessToken(tokenRequestDto);
    }

    @ApiOperation(value="accesstoken  refreshtoken 재발급")
    @PostMapping("/reissueAccessRefreshToken")
    public TokenDto  reissueAccessRefreshToken(@RequestBody TokenRequestDto tokenRequestDto){
        return authService.reissueAccessRefreshToken(tokenRequestDto);
    }
}
