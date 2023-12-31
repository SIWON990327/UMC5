package com.example.umc1.domain.post.service;

import com.example.umc1.domain.member.entity.Member;
import com.example.umc1.domain.member.repository.MemberRepository;
import com.example.umc1.domain.post.dto.PostRequestDto;
import com.example.umc1.domain.post.dto.PostResponseDto;
import com.example.umc1.domain.post.dto.PostUpdateRequestDto;
import com.example.umc1.domain.post.entity.Post;
import com.example.umc1.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void createPost(PostRequestDto requestDto) {
        Member author = memberRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다"));
        Post post = Post.builder()
                .author(author)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
        postRepository.save(post);
    }

    public List<PostResponseDto> getPosts(Long authorId) {
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다"));
        List<Post> posts = postRepository.findByAuthor(author);
        List<PostResponseDto> dtos =posts.stream()
                .map(PostResponseDto::of)
                .collect(Collectors.toList());
        return dtos;
    }

    public void updatePost(Long postId, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 정보를 찾을 수 없습니다."));
        post.updatePost(requestDto);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 정보를 찾을 수 없습니다."));
        postRepository.delete(post);
    }

}
