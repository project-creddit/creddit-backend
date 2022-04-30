package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"좋아요 생성/삭제"})
@RequiredArgsConstructor
@RestController
public class LikeApiController {

    private final LikeService likeService;

    @ApiOperation(value = "좋아요", notes = "좋아요가 없다면 생성, 있다면 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "글 혹은 댓글 ID"),
            @ApiImplicitParam(name = "type", value = "ID 타입 ex) post, comment")
    })
    @PostMapping("/like/{id}")
    public void clickLike(@PathVariable Long id, @RequestParam String type){
        likeService.clickLike(id, type);
    }

}
