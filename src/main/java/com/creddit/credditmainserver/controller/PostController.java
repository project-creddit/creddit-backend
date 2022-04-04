package com.creddit.credditmainserver.controller;

import com.creddit.credditmainserver.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "글 관련 화면 요청")
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "글 작성페이지 요청")
    @GetMapping("/post/write")
    public String createPostForm(){
        return "";
    }
}
