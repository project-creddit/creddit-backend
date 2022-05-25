package com.creddit.credditmainserver.api;


import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.dto.request.PostRequestDto;
import com.creddit.credditmainserver.dto.request.ProfileRequestDto;
import com.creddit.credditmainserver.dto.response.ProfileResponseDto;
import com.creddit.credditmainserver.service.AwsS3Service;
import com.creddit.credditmainserver.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Api(tags= "프로필")
@RestController
@RequiredArgsConstructor
public class ProfileApiController {
    private final ProfileService profileService;
    private final AwsS3Service awsS3Service;

    @ApiOperation(value = "닉네임을 통한 프로필 출력")
    @GetMapping("/profile/show/{nickname}")
    public ProfileResponseDto getProfile(@PathVariable String nickname){
        return profileService.getProfileByNickname(nickname);
    }

    @ApiOperation(value = "프로필 출력")
    @GetMapping("/profile/show")
    public ProfileResponseDto getProfile(Principal principal){
        String id = principal.getName();
        return profileService.getProfile(Long.parseLong(id));
    }



    @PostMapping(value = "/profile/test")
    public void test(@RequestPart(value = "image", required = false) MultipartFile file){
        if(file == null){
            System.out.println("file is null");
        }
        else if(file.isEmpty()){
            System.out.println("file is Empty");
        }


    }

    @ApiOperation(value = "프로필 생성/수정")
    @PostMapping(value = "/profile/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ProfileResponseDto saveProfile(Principal principal,
                                          @RequestPart(value = "image", required = false) MultipartFile file,
                                          @Valid @RequestPart ProfileRequestDto profileRequestDto) throws IOException {
        Long id = Long.parseLong(principal.getName());
        checkExistImgAndDelete(profileService.findById(id).getImgName());

        return profileService.saveProfile(id, profileRequestDto, file);
    }

    private void checkExistImgAndDelete(String savedImgName) {
        if (savedImgName != null && savedImgName != "empty") {
            awsS3Service.deleteFile(savedImgName);
        }
    }

}
