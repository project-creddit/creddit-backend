package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.dto.request.LikeRequestDto;
import com.creddit.credditmainserver.service.LikeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"좋아요 저장/삭제"})
@RequiredArgsConstructor
@RestController
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/like")
    public Long createLike(@RequestBody LikeRequestDto likeRequestDto){
        return likeService.createLike(likeRequestDto);
    }

    @DeleteMapping("/like/{id}")
    public void deleteLike(@PathVariable Long id){
        likeService.deleteLike(id);
    }
}
