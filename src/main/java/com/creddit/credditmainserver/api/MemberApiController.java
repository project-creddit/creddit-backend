package com.creddit.credditmainserver.api;
import javax.validation.Valid;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.request.MemberRequest;
import com.creddit.credditmainserver.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"회원가입/로그인"})
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @ApiOperation(value="회원가입 / 하나라도 미입력 시 status : 400" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email"),
            @ApiImplicitParam(name = "password"),
            @ApiImplicitParam(name = "nickname")
    })
    @PostMapping("/member/signup")
    public Long signUp(@RequestBody @Valid MemberRequest request){
        return memberService.join(request);
    }

    @ApiOperation(value="닉네임 중복 체크")
    @GetMapping("/member/checkDuplicate/{nickname}")
    public boolean checkDuplicateByNickname(@PathVariable("nickname") String nickname){
        return memberService.checkDuplicateByNickname(nickname);
    }

    @ApiOperation(value="이메일 중복 체크")
    @GetMapping("/member/checkDuplicate/{email}")
    public boolean checkDuplicateByEmail(@PathVariable("email") String email){
        return memberService.checkDuplicateByEmail(email);
    }
}
