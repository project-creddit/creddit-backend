//package com.creddit.credditmainserver.service;
//
//import com.creddit.credditmainserver.domain.Authority;
//import com.creddit.credditmainserver.domain.Member;
//import com.creddit.credditmainserver.domain.Post;
//import com.creddit.credditmainserver.dto.request.PostRequestDto;
//import com.creddit.credditmainserver.repository.MemberRepository;
//import com.creddit.credditmainserver.repository.PostRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Transactional
//@SpringBootTest
//class PostServiceTest {
//
//    @Autowired private PostService postService;
//    @Autowired private PostRepository postRepository;
//    @Autowired private MemberRepository memberRepository;
//
//    @Test
//    @WithMockUser(username = "1", roles = "USER")
//    public void 글_작성() throws Exception{
//        // given
//        String title = "테스트 제목";
//        String content = "테스트 내용";
//
//        Member member = createMember();
//        Long savedPostId = createPost(member, title, content);
//
//        // when
//        List<Post> postList = postRepository.findAll();
//
//        // then
//        Post post = postList.get(0);
//        assertThat(savedPostId).isEqualTo(post.getId());
//        assertThat(member).isEqualTo(post.getMember());
//        assertThat(title).isEqualTo(post.getTitle());
//    }
//
//    @Test
//    @WithMockUser(username = "1", roles = "USER")
//    public void 글_수정() throws Exception{
//        // given
//        Long savedPostId = createPost(createMember(), "테스트 제목", "테스트 내용");
//
//        String expectedTitle = "수정된 제목";
//        String expectedContent = "수정된 내용";
//        MultipartFile file = new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
//
//        Long updatedPostId = postService.updatePost(savedPostId, PostRequestDto.builder()
//                .title(expectedTitle)
//                .content(expectedContent)
//                .build()
//        , file);
//
//        // when
//        List<Post> postList = postRepository.findAll();
//
//        // then
//        Post post = postList.get(0);
//        assertThat(post.getTitle()).isEqualTo(expectedTitle);
//        assertThat(post.getContent()).isEqualTo(expectedContent);
//    }
//
//    @Test
//    @WithMockUser(username = "1", roles = "USER")
//    public void 글_삭제() throws Exception{
//        // given
//        Long savedPostId = createPost(createMember(), "테스트 제목", "테스트 내용");
//
//        // when
//        postService.deletePost(savedPostId);
//
//        // then
//        List<Post> postList = postRepository.findAll();
//        assertThat(postList).isEmpty();
//    }
//
//    private Member createMember(){
//        Member member = Member.toEntity("test@naver.com"
//                                    , "1234"
//                                    , "테스트"
//                                    , Authority.ROLE_USER
//                                    , true);
//
//        return memberRepository.save(member);
//    }
//
//    private Long createPost(Member member, String title, String content) {
//        return postService.createPost(PostRequestDto.builder()
//                .title(title)
//                .content(content)
//                .build());
//    }
//}