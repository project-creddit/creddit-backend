package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.dto.response.FollowListResponseDto;
import com.creddit.credditmainserver.dto.request.PasswordRequestDto;
import com.creddit.credditmainserver.dto.response.PaginationResponseDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.service.EmailSendService;
import com.creddit.credditmainserver.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"회원가입 시 중복 체크 / 팔로우 / 비밀번호찾기 / 검색"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApiController {
    private final EmailSendService emailSendService;
    private final MemberService memberService;

    @Operation(summary = "following list")
    @GetMapping("/follow/list")
    public List<FollowListResponseDto> followList(Principal principal) {
        return memberService.followList(Long.parseLong(principal.getName()));
    }


    @Operation(summary = "팔로우 기능", description = " return: 멤버아이디")
    @PostMapping("/follow")
    public Long follow(@RequestBody String followingNickname, Principal principal) throws Exception {
        return memberService.follw(followingNickname, principal);
    }

    @Operation(summary = "팔로우 해제 기능", description = " return: 멤버아이디")
    @PostMapping("/follow/delete")
    public Long deleteFollow(@RequestBody String followingNickname, Principal principal) {
        return memberService.deleteFollow(followingNickname, principal);
    }

    @Operation(summary = "회원가입 시 이메일 중복 체크", description = "중복 시 return true, 중복 아닐 시 return false")
    @GetMapping("/checkDuplicateByEmail/{email}")
    public boolean checkDuplicateByEmail(@PathVariable String email) {
        return memberService.checkDuplicateByEmail(email);
    }

    @Operation(summary = "닉네임 변경", description = "닉네임 중복 확인 후 변경 필요")
    @PostMapping("/changeNickname")
    public MemberResponseDto changeNickname(@RequestBody String nickname, Principal principal) {
        return memberService.changeNickname(nickname, Long.parseLong(principal.getName()));
    }

    @Operation(summary = "회원가입 시 닉네임 중복 체크", description = "중복 시 return true, 중복 아닐 시 return false")
    @GetMapping("/checkDuplicateByNickname/{nickname}")
    public boolean checkDuplicateByNickname(@PathVariable String nickname) {
        return memberService.checkDuplicateByNickname(nickname);
    }

    @Operation(summary = "임시비밀번호 발송", description = " return: 멤버아이디")
    @PostMapping("/sendEmail/password")
    public Long sendEmail(@RequestBody String email) throws MessagingException {
        return emailSendService.findAndChangPassword(email);
    }

    @Operation(summary = "비밀번호 변경", description = " return: 멤버아이디")
    @PostMapping("/changePassword")
    public Long changePassword(@RequestBody @Valid PasswordRequestDto passwordRequestDto, Principal principal) {
        return memberService.changePassword(passwordRequestDto, Long.parseLong(principal.getName()));
    }

    @ApiOperation(value = "유저 검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "불러올 유저의 개수"),
            @ApiImplicitParam(name = "page", value = "불러올 페이지의 인덱스"),
            @ApiImplicitParam(name = "keyword", value = "검색할 키워드")
    })
    @GetMapping("/search")
    public PaginationResponseDto searchPost(
            final Pageable pageable,
            @RequestParam String keyword,
            Principal principal
    ) {
        Page<Member> pageData = memberService.findUserBySearch(pageable, keyword);
        return new PaginationResponseDto(
                pageData.getTotalPages(),
                pageData.hasNext(),
                pageData.stream().map(MemberResponseDto::new).collect(Collectors.toList())
        );
    }
}
