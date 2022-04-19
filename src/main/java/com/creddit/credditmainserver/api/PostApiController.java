package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.dto.request.PostRequestDto;
import com.creddit.credditmainserver.dto.response.PostResponseDto;
import com.creddit.credditmainserver.service.AwsS3Service;
import com.creddit.credditmainserver.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Api(tags = {"글 작성/수정/삭제"})
@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;
    private final AwsS3Service awsS3Service;

    @ApiOperation(value = "전체 글 조회")
    @GetMapping("/post")
    public List<PostResponseDto> getPostPage(@RequestParam Long lastPostId, @RequestParam int size){
        return postService.fetchPostPagesBy(lastPostId, size);
    }

    @ApiOperation(value = "특정 글 조회")
    @GetMapping("/post/{id}")
    public PostResponseDto selectOnePost(@PathVariable Long id){
        return postService.findById(id);
    }

    @ApiOperation(value = "글 검색")
    @GetMapping("/post/search")
    public List<PostResponseDto> searchPost(
            @RequestParam Long lastPostId,
            @RequestParam int size,
            @RequestParam String keyword
    ){
        return postService.searchPostByKeyword(lastPostId, size, keyword);
    }

    @ApiOperation(value = "글 작성"
                , notes = "제목, 내용 필수값 / null, '', ' ' 모두 불가능")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image"),
            @ApiImplicitParam(name = "title"),
            @ApiImplicitParam(name = "content")
    })
    @PostMapping(value = "/post/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Long createPost(
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart(value = "requestDto") PostRequestDto postRequestDto
    ){
        if(file != null){
            postRequestDto = imageUpload(file, postRequestDto);
        }

        return postService.createPost(postRequestDto);
    }

    @ApiOperation(value = "글 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image"),
            @ApiImplicitParam(name = "title"),
            @ApiImplicitParam(name = "content")
    })
    @PostMapping("/post/{id}/edit")
    public Long updatePost(
            @PathVariable Long id,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart(value = "requestDto") PostRequestDto postRequestDto
    ){
        String savedImgName = postService.findById(id).getImgName();

        if(file != null){
            checkExistImgAndDelete(savedImgName);
            postRequestDto = imageUpload(file, postRequestDto);
        }
        return postService.updatePost(id, postRequestDto);
    }

    @ApiOperation(value = "글 삭제")
    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id){
        String savedImgName = postService.findById(id).getImgName();

        checkExistImgAndDelete(savedImgName);
        postService.deletePost(id);
    }

    private void checkExistImgAndDelete(String savedImgName) {
        if (savedImgName != null) {
            awsS3Service.deleteFile(savedImgName);
        }
    }

    private PostRequestDto imageUpload(MultipartFile file, PostRequestDto postRequestDto) {
        Map<String, String> imgInfo = awsS3Service.upload(file, "post");
        postRequestDto.addImgName(imgInfo.get("imgName"));
        postRequestDto.addImgUrl(imgInfo.get("imgUrl"));

        return postRequestDto;
    }
}
