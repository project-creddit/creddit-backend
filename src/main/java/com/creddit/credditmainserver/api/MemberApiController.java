package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.domain.Follower;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Api(tags = {"회원가입 시 중복 체크"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApiController {

    private final MemberService memberService;


    @PostMapping("/follow")
    public Long follow(@RequestBody  String followingNickname, Principal principal) throws Exception {
        return memberService.follw(followingNickname,principal);
    }

    @PostMapping("/follow/delete")
    public Long deleteFollow(@RequestBody  String followingNickname, Principal principal){
        return memberService.deleteFollow(followingNickname,principal);
    }

    @Operation(summary="회원가입 시 이메일 중복 체크",description="중복 시 return true, 중복 아닐 시 return false")
    @GetMapping("/checkDuplicateByEmail/{email}")
    public boolean checkDuplicateByEmail(@PathVariable String email){
        return memberService.checkDuplicateByEmail(email);
    }

    @Operation(summary="닉네임 변경", description = "닉네임 중복 확인 후 변경 필요")
    @PostMapping("/changeNickname")
    public MemberResponseDto changeNickname(@RequestBody String nickname, Principal principal){
        return memberService.changeNickname(nickname,Long.parseLong(principal.getName()));
    }

    @Operation(summary="회원가입 시 닉네임 중복 체크",description="중복 시 return true, 중복 아닐 시 return false")
    @GetMapping("/checkDuplicateByNickname/{nickname}")
    public boolean checkDuplicateByNickname(@PathVariable String nickname){
        return memberService.checkDuplicateByNickname(nickname);
    }


}
