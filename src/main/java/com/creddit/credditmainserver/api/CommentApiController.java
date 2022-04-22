package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.dto.request.CommentRequestDto;
import com.creddit.credditmainserver.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"댓글 작성/수정/삭제"})
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성", notes = "글 번호, 내용 필수값 / 내용 null, '', ' ' 모두 불가능")
    @ApiImplicitParam(name = "requestDto", value = "글 ID, 부모 댓글 ID, 내용")
    @PostMapping("/comment")
    public Long createComment(@RequestBody CommentRequestDto commentRequestDto){
        return commentService.createComment(commentRequestDto);
    }

    @ApiOperation(value = "댓글 수정")
    @ApiImplicitParam(name = "content", value = "수정할 내용")
    @PostMapping("/comment/{id}")
    public Long updateComment(@PathVariable Long id, String content){
        return commentService.updateComment(id, content);
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
    }

}
