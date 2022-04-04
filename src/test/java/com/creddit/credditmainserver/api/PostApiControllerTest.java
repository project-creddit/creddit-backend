package com.creddit.credditmainserver.api;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.PostSaveRequestDto;
import com.creddit.credditmainserver.repository.PostRepository;
import com.creddit.credditmainserver.service.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostApiControllerTest {

    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;

    @AfterEach
    public void tearDown() throws Exception{
        postRepository.deleteAll();
    }
    
    /*@Test
    public void 글_작성() throws Exception{
        // given
        Member member = createMember();
        String title = "테스트 제목";
        String content = "테스트 내용";

        Long savedPostId = postService.createPost(PostSaveRequestDto.builder()
                .member(member)
                .title(title)
                .content(content)
                .build());

        String url = "http://localhost:" + port +"/post/write";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, postRepository.findById(savedPostId), Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(0L);

        List<Post> postList = postRepository.findAll();
        Post post = postList.get(0);
        assertThat(post.getMember()).isEqualTo(member);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }*/
}