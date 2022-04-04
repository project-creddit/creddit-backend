package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.PostSaveRequestDto;
import com.creddit.credditmainserver.dto.request.PostUpdateRequestDto;
import com.creddit.credditmainserver.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PostServiceTest {

    @Autowired private EntityManager em;
    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;

    @Test
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

        // when
        List<Post> postList = postRepository.findAll();
        
        // then
        Post post = postList.get(0);
        assertThat(savedPostId).isEqualTo(post.getId());
        assertThat(member).isEqualTo(post.getMember());
        assertThat(title).isEqualTo(post.getTitle());
    }

    @Test
    public void 글_수정() throws Exception{
        // given
        Member member = createMember();

        Long savedPostId = postService.createPost(PostSaveRequestDto.builder()
                .member(member)
                .title("테스트 제목")
                .content("테스트 내용")
                .build());

        String expectedTitle = "수정된 제목";
        String expectedContent = "수정된 내용";

        Long updatedPostId = postService.updatePost(savedPostId, PostUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build());

        // when
        List<Post> postList = postRepository.findAll();

        // then
        Post post = postList.get(0);
        assertThat(post.getTitle()).isEqualTo(expectedTitle);
        assertThat(post.getContent()).isEqualTo(expectedContent);
    }

    @Test
    public void 글_삭제() throws Exception{
        // given
        Long savedPostId = postService.createPost(PostSaveRequestDto.builder()
                .member(createMember())
                .title("테스트 제목")
                .content("테스트 내용")
                .build());

        // when
        postService.deletePost(savedPostId);

        // then
        List<Post> postList = postRepository.findAll();
        assertThat(postList).isEmpty();
    }

    private Member createMember(){
        Member member = new Member();
        member.setNickname("테스트 닉네임");
        em.persist(member);

        return member;
    }
}