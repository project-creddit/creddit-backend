package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.dto.request.PostRequestDto;
import com.creddit.credditmainserver.dto.response.PostResponseDto;
import com.creddit.credditmainserver.service.AwsS3Service;
import com.creddit.credditmainserver.service.PostService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"글 작성/수정/삭제"})
@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;
    private final AwsS3Service awsS3Service;

    @ApiOperation(value = "메인화면 글 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "index", value = "최신 or 팔로잉 정렬 : 마지막 글의 ID, 좋아요 정렬 : 페이지 번호"),
            @ApiImplicitParam(name = "size", value = "불러올 글의 개수"),
            @ApiImplicitParam(name = "sort", value = "정렬 기준 ex) new, like, following")
    })
    @GetMapping("/post")
    public List<PostResponseDto> getPosts(
            @RequestParam Long index,
            @RequestParam int size,
            @RequestParam String sort
    ){
        return postService.getPosts(index, size, sort);
    }

    @ApiOperation(value = "글 상세화면 조회")
    @GetMapping("/post/{id}")
    public PostResponseDto selectOnePost(@PathVariable Long id){
        return postService.findById(id);
    }

    @ApiOperation(value = "특정 유저가 작성한 글 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "index", value = "최신 정렬 : 마지막 글의 ID, 좋아요 정렬 : 페이지 번호"),
            @ApiImplicitParam(name = "size", value = "불러올 글의 개수"),
            @ApiImplicitParam(name = "sort", value = "정렬 기준 ex) new, like"),
            @ApiImplicitParam(name = "nickname", value = "유저 닉네임")
    })
    @GetMapping("/post/user/{nickname}")
    public List<PostResponseDto> getPostByUser(
            @RequestParam Long index,
            @RequestParam int size,
            @RequestParam String sort,
            @PathVariable String nickname
    ){
        return  postService.getPostByUser(index, size, sort, nickname);
    }

    @ApiOperation(value = "글 검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "index", value = "최신 or 팔로잉 정렬 : 마지막 글의 ID, 좋아요 정렬 : 페이지 번호"),
            @ApiImplicitParam(name = "size", value = "불러올 글의 개수"),
            @ApiImplicitParam(name = "sort", value = "정렬 기준 ex) new, like, following"),
            @ApiImplicitParam(name = "keyword", value = "검색할 키워드")
    })
    @GetMapping("/post/search")
    public List<PostResponseDto> searchPosts(
            @RequestParam Long index,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam String keyword
    ){
        return postService.searchPosts(index, size, sort, keyword);
    }

    @ApiOperation(value = "글 작성", notes = "제목, 내용 필수값 / null, '', ' ' 불가능")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "업로드할 이미지"),
            @ApiImplicitParam(name = "requestDto", value = "제목, 내용")
    })
    @PostMapping(value = "/post/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Long createPost(
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart(value = "requestDto") PostRequestDto postRequestDto
    ){
        if(file != null && !file.isEmpty()){
            imageUpload(file, postRequestDto);
        }

        return postService.createPost(postRequestDto);
    }

    @ApiOperation(value = "글 수정", notes = "제목, 내용 필수값 / null, '', ' ' 불가능")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "수정할 이미지"),
            @ApiImplicitParam(name = "requestDto", value = "제목, 내용")
    })
    @PostMapping("/post/{id}/edit")
    public Long updatePost(
            @PathVariable Long id,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart(value = "requestDto") PostRequestDto postRequestDto
    ){
        postService.isSameWriter(id, "수정");
        checkEmptyFileAndUploadImg(id, file, postRequestDto);

        return postService.updatePost(id, postRequestDto, file);
    }

    @ApiOperation(value = "글 삭제")
    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id){
        postService.isSameWriter(id, "삭제");
        checkExistImgAndDelete(id);
        postService.deletePost(id);
    }

    private void checkEmptyFileAndUploadImg(Long id, MultipartFile file, PostRequestDto postRequestDto) {
        if(file != null){
            checkExistImgAndDelete(id);

            if(!file.isEmpty()){
                imageUpload(file, postRequestDto);
            }
        }
    }

    private void checkExistImgAndDelete(Long id) {
        String savedImgName = postService.findById(id).getImage().getImgName();

        if (savedImgName != null) {
            awsS3Service.deleteFile(savedImgName);
        }
    }

    private void imageUpload(MultipartFile file, PostRequestDto postRequestDto) {
        Image image = awsS3Service.upload(file, "post");
        postRequestDto.addImage(image);
    }
}
