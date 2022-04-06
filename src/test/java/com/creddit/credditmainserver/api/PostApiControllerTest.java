package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.domain.Authority;
import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.PostSaveRequestDto;
import com.creddit.credditmainserver.dto.request.PostUpdateRequestDto;
import com.creddit.credditmainserver.repository.MemberRepository;
import com.creddit.credditmainserver.repository.PostRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;

    @AfterEach
    void tearDown() throws Exception{
        postRepository.deleteAll();
    }

    @Test
    public void 글_작성() throws Exception{
        // given
        Member member = createMember();
        String title = "테스트 제목";
        String content = "테스트 내용";

        PostSaveRequestDto postSaveRequestDto = createPostSaveRequestDto(member, title, content);

        String url = "http://localhost:" + port +"/post/create";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, postSaveRequestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // then
        List<Post> postList = postRepository.findAll();
        Post post = postList.get(0);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    public void 글_수정() throws Exception{
        // given
        PostSaveRequestDto postSaveRequestDto = createPostSaveRequestDto(createMember(), "테스트 제목", "테스트 내용");
        Long savedPostId = postRepository.save(postSaveRequestDto.toEntity()).getId();

        String expectedTitle = "수정된 제목";
        String expectedContent = "수정된 내용";

        PostUpdateRequestDto postUpdateRequestDto = PostUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port +"/post/ " + savedPostId + "/edit";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, postUpdateRequestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(1L);

        // then
        List<Post> postList = postRepository.findAll();
        Post post = postList.get(0);
        assertThat(post.getTitle()).isEqualTo(expectedTitle);
        assertThat(post.getContent()).isEqualTo(expectedContent);
    }

    private Member createMember(){
        Member member = Member.toEntity("test@naver.com"
                , "1234"
                , "테스트"
                , Authority.ROLE_USER
                , true);

        return memberRepository.save(member);
    }

    private PostSaveRequestDto createPostSaveRequestDto(Member member, String title, String content) {
        return PostSaveRequestDto.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();
    }
}