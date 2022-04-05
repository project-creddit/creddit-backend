package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"회원가입 시 중복 체크"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApiController {

    private final MemberService memberService;

    @Operation(summary="회원가입 시 이메일 중복 체크",description="중복 시 return true, 중복 아닐 시 return false")
    @GetMapping("/checkDuplicateByEmail/{email}")
    public boolean checkDuplicateByEmail(@PathVariable String email){
        return memberService.checkDuplicateByEmail(email);
    }

    @Operation(summary="회원가입 시 닉네임 중복 체크",description="중복 시 return true, 중복 아닐 시 return false")
    @GetMapping("/checkDuplicateByNickname/{nickname}")
    public boolean checkDuplicateByNickname(@PathVariable String nickname){
        return memberService.checkDuplicateByNickname(nickname);
    }
}
