package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.dto.request.CommentRequestDto;
import com.creddit.credditmainserver.dto.response.CommentResponseDto;
import com.creddit.credditmainserver.dto.response.DetailCommentResponseDto;
import com.creddit.credditmainserver.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"댓글 작성/수정/삭제"})
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "글 ID"),
            @ApiImplicitParam(name = "index", value = "최신 정렬 : 마지막 댓글의 ID, 좋아요 정렬 : 페이지 번호"),
            @ApiImplicitParam(name = "size", value = "불러올 댓글의 개수"),
            @ApiImplicitParam(name = "sort", value = "정렬 기준 ex) new, like")
    })
    @GetMapping("/comment")
    public List<CommentResponseDto> getComments(
            @RequestParam Long postId,
            @RequestParam Long index,
            @RequestParam int size,
            @RequestParam String sort
    ){
        return commentService.getComments(postId, index, size, sort);
    }

    @ApiOperation(value = "대댓글 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentCommentId", value = "상위 댓글 ID"),
            @ApiImplicitParam(name = "lastCommentId", value = "마지막 대댓글의 ID"),
            @ApiImplicitParam(name = "size", value = "불러올 댓글의 개수")
    })
    @GetMapping("/comment/detail")
    public List<DetailCommentResponseDto> getDetailComments(
            @RequestParam Long parentCommentId,
            @RequestParam Long lastCommentId,
            @RequestParam int size
    ){
        return commentService.getDetailComments(parentCommentId, lastCommentId, size);
    }

    @ApiOperation(value = "댓글 작성", notes = "글 번호, 내용 필수값 / 내용 null, '', ' ' 모두 불가능")
    @ApiImplicitParam(name = "requestDto", value = "글 ID, 부모 댓글 ID, 내용")
    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto commentRequestDto){
        return commentService.createComment(commentRequestDto);
    }

    @ApiOperation(value = "댓글 수정")
    @ApiImplicitParam(name = "content", value = "수정할 내용")
    @PostMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto){
        commentService.isSameWriter(id, "수정");
        return commentService.updateComment(id, commentRequestDto.getContent());
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable Long id){
        commentService.isSameWriter(id, "삭제");
        commentService.deleteComment(id);
    }

}
