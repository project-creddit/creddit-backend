package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    @Value("${spring.mail.username}")
    private String FROM_ADDRESS ;

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Long findAndChangPassword(String email) throws MessagingException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저 로드 오류 email = "+ email));
        String str =getTempPassword();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setTo(email);
        message.setFrom(FROM_ADDRESS);
        message.setSubject("creddit 임시비밀번호 안내 이메일 입니다.");
        message.setText("안녕하세요. creddit 임시비밀번호 안내 이메일입니다.\n"+"임시 비밀번호는 " + str +" 입니다.");
        mailSender.send(mimeMessage);

        member.setPassword(passwordEncoder.encode(str));
        return memberRepository.save(member).getId();
    }

    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 12; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
}
