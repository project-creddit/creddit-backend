package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.dto.request.PostSaveRequestDto;
import com.creddit.credditmainserver.dto.request.PostUpdateRequestDto;
import com.creddit.credditmainserver.dto.response.PostResponseDto;
import com.creddit.credditmainserver.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"글 작성/수정/삭제"})
@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    @ApiOperation(value = "전체 글 조회")
    @GetMapping("/post")
    public List<PostResponseDto> selectAllPost(){
        return postService.findAllPost();
    }

    @ApiOperation(value = "글 작성"
                , notes = "멤버 아이디, 제목, 내용 필수값 / null, '', ' ' 모두 불가능")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "member"),
            @ApiImplicitParam(name = "title"),
            @ApiImplicitParam(name = "content"),
            @ApiImplicitParam(name = "imgName")
    })
    @PostMapping("/post/create")
    public Long createPost(@RequestBody @Valid PostSaveRequestDto postSaveRequestDto){
        return postService.createPost(postSaveRequestDto);
    }

    @ApiOperation(value = "특정 글 조회"
            , notes = "글 번호로 특정 글 조회")
    @GetMapping("/post/{id}")
    public PostResponseDto selectOnePost(@PathVariable Long id){
        return postService.findById(id);
    }

    @ApiOperation(value = "글 수정")
    @PostMapping("/post/{id}/edit")
    public Long updatePost(@PathVariable Long id, @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        return postService.updatePost(id, postUpdateRequestDto);
    }

    @ApiOperation(value = "글 삭제")
    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id){
        postService.deletePost(id);
    }
}
