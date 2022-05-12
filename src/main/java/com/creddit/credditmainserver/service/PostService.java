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
import org.springframework.web.multipart.MultipartFile;

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
    public Long updatePost(Long id, PostRequestDto postRequestDto, MultipartFile file) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id = " + id));

        if((file != null && file.isEmpty()) || postRequestDto.getImgName() != null){
            post.updatePostAndImage(
                    postRequestDto.getTitle(),
                    postRequestDto.getContent(),
                    postRequestDto.getImgName(),
                    postRequestDto.getImgUrl()
            );
        }else{
            post.updatePost(
                    postRequestDto.getTitle(),
                    postRequestDto.getContent()
            );
        }

        return id;
    }

    @Transactional
    public void deletePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id = " + id));

        postRepository.delete(post);
    }

    public List<PostResponseDto> getPosts(Long index, int size, String sort) {
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Post> posts;

        if(sort.equals("like")){
            int page = Math.toIntExact(index);

            posts = postRepository.findByLikes(PageRequest.of(page, size));
        }else if(sort.equals("following")){
            Long currentMemberId = SecurityUtil.getCurrentMemberId();
            Member member = memberRepository.getById(currentMemberId);

            posts = postRepository.findByFollowing(index, member, pageRequest);
        }else{
            posts = postRepository.findByIdLessThanOrderByIdDesc(index, pageRequest);
        }

        return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    public PostResponseDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id = " + id));

        return new PostResponseDto(post);
    }

    public List<PostResponseDto> searchPosts(Long index, int size, String sort, String keyword){
        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Post> posts;

        if(sort.equals("like")){
            int page = Math.toIntExact(index);

            posts = postRepository.findBySearchAndLikes(keyword, PageRequest.of(page, size));
        }else if(sort.equals("following")){
            Long currentMemberId = SecurityUtil.getCurrentMemberId();
            Member member = memberRepository.getById(currentMemberId);

            posts = postRepository.findBySearchAndFollowing(index, member, keyword, pageRequest);
        }else{
            posts = postRepository.findBySearch(index, keyword, pageRequest);
        }

        return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    public List<PostResponseDto> getPostByUser(Long index, int size, String sort, String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. nickname = " + nickname));

        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Post> posts;

        if(sort.equals("like")){
            int page = Math.toIntExact(index);

            posts = postRepository.findByMemberIdAndLikes(member, PageRequest.of(page, size));
        }else{
            posts = postRepository.findByIdLessThanAndMemberIdOrderByIdDesc(index, member.getId(), pageRequest);
        }

        return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    public void isSameWriter(Long id, String keyword){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다. id = " + id));

        Long postMemberId = post.getMember().getId();
        long currentMemberId = SecurityUtil.getCurrentMemberId();

        if(postMemberId != currentMemberId){
            throw new RuntimeException("작성자만 " + keyword + "할 수 있습니다.");
        }
    }
}
