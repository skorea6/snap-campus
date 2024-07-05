package com.example.snapcampus.controller.api;


import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.dto.request.event.EventAddRequest;
import com.example.snapcampus.dto.request.post.PostAddRequest;
import com.example.snapcampus.service.EventService;
import com.example.snapcampus.service.PostService;
import com.example.snapcampus.service.SecurityService;
import com.example.snapcampus.util.UploadFileUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostApiController {
    private final PostService postService;
    private final SecurityService securityService;

    public PostApiController(PostService postService, SecurityService securityService) {
        this.postService = postService;
        this.securityService = securityService;
    }

    @PostMapping("/add")
    public BaseResponse<Object> addPost(@ModelAttribute @Valid PostAddRequest postAddRequest) {
        securityService.checkIsLogined();

        // 이미지 파일 체크 및 1MB 파일까지 허용
        List<MultipartFile> images = postAddRequest.getImages();
        for (MultipartFile image : images) {
            UploadFileUtil.checkFileIsImage(image);
        }

        String memberUserId = securityService.getMemberUserId();
        String resultMsg = postService.addPost(memberUserId, postAddRequest);
        return new BaseResponse<>(resultMsg);
    }
}
