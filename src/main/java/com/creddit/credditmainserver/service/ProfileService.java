package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Image;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.ProfileRequestDto;
import com.creddit.credditmainserver.dto.response.MemberResponseDto;
import com.creddit.credditmainserver.dto.response.PostResponseDto;
import com.creddit.credditmainserver.dto.response.ProfileResponseDto;
import com.creddit.credditmainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;
    private final AwsS3Service awsS3Service;
    public ProfileResponseDto saveProfile(Long id, ProfileRequestDto profileRequestDto, MultipartFile file) {

        Member member = findById(id);
        Image image = new Image();

        if(file == null){
            image.setImgName("empty");
            image.setImgUrl("");
        }
        else if(!file.isEmpty()){
            image = awsS3Service.upload(file, "post");
        }
        else{
           image.setImgName(member.getImgName());
           image.setImgUrl(member.getImgUrl());
        }
        profileRequestDto.setImage(image);

        member.setProfile(profileRequestDto.getImage().getImgUrl(), profileRequestDto.getImage().getImgName(),profileRequestDto.getIntroduction());
        memberRepository.save(member);

        return new ProfileResponseDto(member);
    }

    public ProfileResponseDto getProfile(Long id){
        Member member = findById(id);
        return new ProfileResponseDto(member);
    }

    public ProfileResponseDto getProfileByNickname(String nickname){
        Member member = memberRepository.findByNickname(nickname).orElseThrow(()-> new IllegalArgumentException("프로필 로드 오류 nickname =" + nickname));
        return new ProfileResponseDto(member);
    }

    public Member findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("프로필 로드 오류 memberId =" + id));
        return member;
    }
}
