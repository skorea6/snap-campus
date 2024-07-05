package com.example.snapcampus.service;

import com.example.snapcampus.dto.request.post.PostAddRequest;
import com.example.snapcampus.dto.response.post.PostAggregateDtoResponse;
import com.example.snapcampus.dto.response.post.PostDetailDtoResponse;
import com.example.snapcampus.entity.Event;
import com.example.snapcampus.entity.Post;
import com.example.snapcampus.repository.EventRepository;
import com.example.snapcampus.repository.MemberRepository;
import com.example.snapcampus.repository.PostRepository;
import com.example.snapcampus.util.RandomUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final AwsS3Service awsS3Service;

    public List<PostDetailDtoResponse> getAllPosts(){
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        AtomicLong counter = new AtomicLong(1);
        return posts.stream().map(post -> {
            long currentIndex = counter.getAndIncrement();
            currentIndex = (currentIndex - 1) % 4 + 1;
            return post.toDetailDto(currentIndex);
        }).toList();
    }

    public PostAggregateDtoResponse getPost(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 사진입니다.")
        );

        return post.toAggregateDto();
    }

    public String addPost(String memberUserId, PostAddRequest postAddRequest){
        Event event = eventRepository.findById(postAddRequest.getEventId()).orElseThrow(
                () -> new IllegalArgumentException("eventId를 찾을 수 없습니다.")
        );

        Post entity = postAddRequest.toEntity();
        entity.setMember(memberRepository.findByUserId(memberUserId));
        entity.setEvent(event);
        entity.setMap(event.getMap());

        // 이미지 S3 업로드
        String path = "post/images/";

        List<String> images = new ArrayList<>();
        List<MultipartFile> requestImages = postAddRequest.getImages();

        for (MultipartFile image : requestImages) {
            String fileName = RandomUtil.generateRandomString(32);
            String newFileName = awsS3Service.uploadFile(image, path, fileName);
            images.add(path + newFileName);
        }

        entity.setImages(images);

        postRepository.save(entity);
        return "사진이 추가되었습니다.";
    }
}
