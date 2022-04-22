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
import java.security.Principal;

@Api(tags= "프로필")
@RestController
@RequiredArgsConstructor
public class ProfileApiController {
    private final ProfileService profileService;
    private final AwsS3Service awsS3Service;

    @ApiOperation(value = "프로필 출력")
    @GetMapping("/profile/show")
    public ProfileResponseDto getProfile(Principal principal){
        String id = principal.getName();
        return profileService.getProfile(Long.parseLong(id));
    }

    @ApiOperation(value = "프로필 생성/수정")
    @PostMapping(value = "/profile/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ProfileResponseDto saveProfile(Principal principal,
                                          @RequestPart(value = "image", required = false) MultipartFile file,
                                          @Valid @RequestBody ProfileRequestDto profileRequestDto){
        Long id = Long.parseLong(principal.getName());

        if(file!= null){
            checkExistImgAndDelete(profileService.findById(id).getImgName());

            Image image = awsS3Service.upload(file, "post");
            profileRequestDto.setImage(image);
        }

        return profileService.saveProfile(id, profileRequestDto);
    }

    private void checkExistImgAndDelete(String savedImgName) {
        if (savedImgName != null) {
            awsS3Service.deleteFile(savedImgName);
        }
    }

}
