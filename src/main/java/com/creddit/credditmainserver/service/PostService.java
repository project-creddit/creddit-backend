package com.creddit.credditmainserver.service;

import com.creddit.credditmainserver.domain.Post;
import com.creddit.credditmainserver.dto.request.PostSaveRequestDto;
import com.creddit.credditmainserver.dto.request.PostUpdateRequestDto;
import com.creddit.credditmainserver.dto.response.PostResponseDto;
import com.creddit.credditmainserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(PostSaveRequestDto postSaveRequestDto){
        return postRepository.save(postSaveRequestDto.toEntity()).getId();
    }

    @Transactional
    public Long updatePost(Long id, PostUpdateRequestDto postUpdateRequestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id = " + id));

        post.updatePost(postUpdateRequestDto.getTitle(), postUpdateRequestDto.getContent(), postUpdateRequestDto.getImgName());

        return id;
    }

    @Transactional
    public void deletePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id = " + id));

        postRepository.delete(post);
    }

    public List<PostResponseDto> findAllPost(){
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public PostResponseDto findById(Long id) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id = " + id));

        return new PostResponseDto(entity);
    }
}