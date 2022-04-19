package com.creddit.credditmainserver.api;


import com.creddit.credditmainserver.dto.request.ProfileRequestDto;
import com.creddit.credditmainserver.dto.response.ProfileResponseDto;
import com.creddit.credditmainserver.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ProfileApiController {
    private final ProfileService profileService;


    @PostMapping("/profile/show")
    public ProfileResponseDto getProfile(Principal principal){
        String id = principal.getName();
        return profileService.getProfile(Long.parseLong(id));
    }



    @PostMapping("/profile/post")
    public ProfileResponseDto saveProfile(Principal principal, @Valid @RequestBody ProfileRequestDto profileRequestDto){
        String id = principal.getName();
        return profileService.saveProfile(Long.parseLong(id),profileRequestDto);
    }
}
