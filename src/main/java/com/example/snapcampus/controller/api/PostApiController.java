package com.example.snapcampus.controller.api;


import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.dto.request.post.PostAddDtoRequest;
import com.example.snapcampus.dto.request.post.PostDeleteDtoRequest;
import com.example.snapcampus.dto.request.post.PostUpdateDtoRequest;
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
    public BaseResponse<Object> addPost(@ModelAttribute @Valid PostAddDtoRequest postAddDtoRequest) {
        securityService.checkIsLogined();

        // 이미지 파일 체크 및 1MB 파일까지 허용
        List<MultipartFile> images = postAddDtoRequest.getImages();
        if(images.size() > 8){
            throw new IllegalArgumentException("사진은 최대 8개까지 업로드 가능합니다.");
        }
        for (MultipartFile image : images) {
            UploadFileUtil.checkFileIsImage(image);
        }

        String memberUserId = securityService.getMemberUserId();
        String resultMsg = postService.addPost(memberUserId, postAddDtoRequest);
        return new BaseResponse<>(resultMsg);
    }

    @PostMapping("/update")
    public BaseResponse<Object> updatePost(@ModelAttribute @Valid PostUpdateDtoRequest postUpdateDtoRequest) {
        securityService.checkIsLogined();

        // 이미지 파일 체크 및 1MB 파일까지 허용
        List<MultipartFile> images = postUpdateDtoRequest.getImages();
        if(images != null){
            if(images.size() > 8){
                throw new IllegalArgumentException("사진은 최대 8개까지 업로드 가능합니다.");
            }
            for (MultipartFile image : images) {
                UploadFileUtil.checkFileIsImage(image);
            }
        }

        String memberUserId = securityService.getMemberUserId();
        String resultMsg = postService.updatePost(memberUserId, postUpdateDtoRequest);
        return new BaseResponse<>(resultMsg);
    }

    @PostMapping("/delete")
    public BaseResponse<Object> deletePost(@RequestBody @Valid PostDeleteDtoRequest postDeleteDtoRequest) {
        securityService.checkIsLogined();

        String memberUserId = securityService.getMemberUserId();
        String resultMsg = postService.deletePost(memberUserId, postDeleteDtoRequest);
        return new BaseResponse<>(resultMsg);
    }
}
