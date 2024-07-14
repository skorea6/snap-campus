package com.example.snapcampus.service;


import com.example.snapcampus.dto.request.post.PostLikeDtoRequest;
import com.example.snapcampus.dto.response.post.PostDetailDtoResponse;
import com.example.snapcampus.dto.response.post.PostLikeDtoResponse;
import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.Post;
import com.example.snapcampus.entity.PostLike;
import com.example.snapcampus.repository.MemberRepository;
import com.example.snapcampus.repository.PostLikeRepository;
import com.example.snapcampus.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@RequiredArgsConstructor
@Transactional
@Service
public class PostLikeService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public List<PostDetailDtoResponse> list(String memberUserId){
        Member member = memberRepository.findByUserId(memberUserId);
        Page<PostLike> postLikes = postLikeRepository.findAllByMember(defaultPageRequest(), member);

        AtomicLong counter = new AtomicLong(1);
        return postLikes.stream().map(PostLike::getPost).map(post -> post.toDetailDto(counter)).toList();
    }


    public PostLikeDtoResponse update(String memberUserId, PostLikeDtoRequest postLikeDtoRequest){
        Post post = postRepository.findById(postLikeDtoRequest.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 사진입니다.")
        );

        boolean status = false;
        Long likeCount = post.getLikeCount();

        Member member = memberRepository.findByUserId(memberUserId);
        PostLike findPostLike = postLikeRepository.findByMemberAndPost(member, post);

        if(findPostLike != null){ // 좋아요 취소
            postLikeRepository.deleteById(findPostLike.getId()); // postLike 제거
            likeCount--;
        }else{ // 좋아요 누름
            postLikeRepository.save(new PostLike(member, post)); // postLike 추가
            likeCount++;
            status = true;
        }

        post.setLikeCount(likeCount); // post > likeCount 업데이트
        postRepository.save(post);

        return new PostLikeDtoResponse(likeCount, status);
    }

    private static Pageable defaultPageRequest() {
        return PageRequest.of(
                0,
                32,
                Sort.by(Sort.Order.desc("createdAt"))
        );
    }
}
