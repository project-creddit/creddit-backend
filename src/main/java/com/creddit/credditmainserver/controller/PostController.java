package com.creddit.credditmainserver.controller;

import com.creddit.credditmainserver.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
}
