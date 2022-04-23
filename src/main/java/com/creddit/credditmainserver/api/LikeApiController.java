package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.dto.request.LikeRequestDto;
import com.creddit.credditmainserver.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"좋아요 생성/삭제"})
@RequiredArgsConstructor
@RestController
public class LikeApiController {

    private final LikeService likeService;

    @ApiOperation(value = "글 좋아요 생성")
    @ApiImplicitParam(name = "requestDto", value = "글 ID")
    @PostMapping("/like/post")
    public Long createPostLike(@RequestBody LikeRequestDto likeRequestDto){
        return likeService.createPostLike(likeRequestDto);
    }

    @ApiOperation(value = "댓글 좋아요 생성")
    @ApiImplicitParam(name = "requestDto", value = "댓글 ID")
    @PostMapping("/like/comment")
    public Long createCommentLike(@RequestBody LikeRequestDto likeRequestDto){
        return likeService.createCommentLike(likeRequestDto);
    }

    @ApiOperation(value = "좋아요 삭제", notes = "글 or 댓글 ID 필수")
    @DeleteMapping("/like/{id}")
    public void deleteLike(@PathVariable Long id){
        likeService.deleteLike(id);
    }
}
