package com.example.snapcampus.service;

import com.example.snapcampus.dto.request.post.PostAddDtoRequest;
import com.example.snapcampus.dto.request.post.PostDeleteDtoRequest;
import com.example.snapcampus.dto.request.post.PostUpdateDtoRequest;
import com.example.snapcampus.dto.response.post.PostAggregateDtoResponse;
import com.example.snapcampus.dto.response.post.PostDetailDtoResponse;
import com.example.snapcampus.entity.Event;
import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.Post;
import com.example.snapcampus.entity.PostLike;
import com.example.snapcampus.repository.EventRepository;
import com.example.snapcampus.repository.MemberRepository;
import com.example.snapcampus.repository.PostLikeRepository;
import com.example.snapcampus.repository.PostRepository;
import com.example.snapcampus.util.RandomUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final AwsS3Service awsS3Service;

    public List<PostDetailDtoResponse> getAllPosts(){
        Page<Post> posts = postRepository.findAllBy(defaultPageRequest());

        AtomicLong counter = new AtomicLong(1);
        return posts.stream().map(post -> post.toDetailDto(counter)).toList();
    }

    public List<PostDetailDtoResponse> getMyPosts(String memberUserId){
        Member member = memberRepository.findByUserId(memberUserId);
        Page<Post> posts = postRepository.findAllByMember(defaultPageRequest(), member);

        AtomicLong counter = new AtomicLong(1);
        return posts.stream().map(post -> post.toDetailDto(counter)).toList();
    }

    public List<PostDetailDtoResponse> getLikeTopPosts(){
        Page<Post> posts = postRepository.findAllBy(PageRequest.of(
                0,
                4,
                Sort.by(Sort.Order.desc("likeCount"))
        ));

        AtomicLong counter = new AtomicLong(1);
        return posts.stream().map(post -> post.toDetailDto(counter)).toList();
    }

    public List<PostDetailDtoResponse> getSearchPosts(String keyword){
        Page<Post> posts = postRepository.findAllByTitleContaining(defaultPageRequest(), keyword);

        AtomicLong counter = new AtomicLong(1);
        return posts.stream().map(post -> post.toDetailDto(counter)).toList();
    }

    public PostAggregateDtoResponse getPost(String memberUserId, Long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 사진입니다.")
        );

        PostAggregateDtoResponse aggregateDto = post.toAggregateDto();
        if(memberUserId != null){
            PostLike postLike = postLikeRepository.findByMemberAndPost(
                    memberRepository.findByUserId(memberUserId),
                    post
            );
            if(postLike != null) aggregateDto.setLikeStatus(true); // Like 을 누른 내역이 있다면, likeStatus를 true로.
        }

        return aggregateDto;
    }

    public String addPost(String memberUserId, PostAddDtoRequest postAddDtoRequest){
        Event event = eventRepository.findById(postAddDtoRequest.getEventId()).orElseThrow(
                () -> new IllegalArgumentException("eventId를 찾을 수 없습니다.")
        );

        Post entity = postAddDtoRequest.toEntity();
        entity.setMember(memberRepository.findByUserId(memberUserId));
        entity.setEvent(event);
        entity.setMap(event.getMap());

        // 이미지 S3 업로드
        List<String> images = uploadPostImages(postAddDtoRequest.getImages());
        entity.setImages(images);

        postRepository.save(entity);
        return "사진이 추가되었습니다.";
    }

    public String updatePost(String memberUserId, PostUpdateDtoRequest postUpdateDtoRequest){
        Post post = postRepository.findById(postUpdateDtoRequest.getId()).orElseThrow(
                () -> new IllegalArgumentException("id를 찾을 수 없습니다.")
        );

        if(!memberUserId.equals(post.getMember().getUserId())){
            throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
        }

        // entity update
        post.update(postUpdateDtoRequest.getTitle(), postUpdateDtoRequest.getDescription(), postUpdateDtoRequest.getDepartment());

        if(postUpdateDtoRequest.getImages() != null){
            // 기존 이미지 전부 삭제
            post.getImages().forEach(awsS3Service::deleteFile);

            // 이미지 S3 업로드
            List<String> images = uploadPostImages(postUpdateDtoRequest.getImages());
            post.setImages(images);
        }

        postRepository.save(post); // save entity to database
        return "사진 정보가 업데이트 되었습니다.";
    }

    public String deletePost(String memberUserId, PostDeleteDtoRequest postDeleteDtoRequest){
        Post post = postRepository.findById(postDeleteDtoRequest.getId()).orElseThrow(
                () -> new IllegalArgumentException("id를 찾을 수 없습니다.")
        );

        if(!memberUserId.equals(post.getMember().getUserId())){
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
        }

        post.getImages().forEach(awsS3Service::deleteFile); // s3 delete image files
        postRepository.deleteById(postDeleteDtoRequest.getId()); // delete on database

        return "사진이 삭제 되었습니다.";
    }


    private List<String> uploadPostImages(List<MultipartFile> requestImages) {
        String path = "post/images/";
        List<String> images = new ArrayList<>();

        for (MultipartFile image : requestImages) {
            String fileName = RandomUtil.generateRandomString(32);
            String newFileName = awsS3Service.uploadFile(image, path, fileName);
            images.add(path + newFileName);
        }
        return images;
    }


    private static Pageable defaultPageRequest() {
        return PageRequest.of(
                0,
                32,
                Sort.by(Sort.Order.desc("createdAt"))
        );
    }
}
