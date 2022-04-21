package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Member;
import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.PostRequestDto;
import com.creddit.credditmainserver.dto.response.PostResponseDto;
import com.creddit.credditmainserver.login.security.SecurityUtil;
import com.creddit.credditmainserver.repository.MemberRepository;
import com.creddit.credditmainserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createPost(PostRequestDto postRequestDto){
        Member member = memberRepository.getById(SecurityUtil.getCurrentMemberId());

        return postRepository.save(postRequestDto.toEntity(member)).getId();
    }

    @Transactional
    public Long updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id = " + id));

        post.updatePost(
                postRequestDto.getTitle(),
                postRequestDto.getContent(),
                postRequestDto.getImage().getImgName(),
                postRequestDto.getImage().getImgUrl()
        );

        return id;
    }

    @Transactional
    public void deletePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id = " + id));

        postRepository.delete(post);
    }

    public List<PostResponseDto> fetchPostPagesBy(Long lastPostId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Post> posts = postRepository.findByIdLessThanOrderByIdDesc(lastPostId, pageRequest);

        return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    public PostResponseDto findById(Long id) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id = " + id));

        return new PostResponseDto(entity);
    }

    public List<PostResponseDto> searchPostByKeyword(Long lastPostId, int size, String keyword){
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Post> posts = postRepository.findByPageOfSearching(lastPostId, keyword, pageRequest);

        return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }
}
