package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.repository.MemberCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberCustomRepository memberRepository;

    @Transactional(readOnly = true)
    public boolean checkDuplicateByEmail(String email){
        return memberRepository.checkDuplicateByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicateByNickname(String nickname){
        return memberRepository.checkDuplicateByNickname(nickname);
    }
}
