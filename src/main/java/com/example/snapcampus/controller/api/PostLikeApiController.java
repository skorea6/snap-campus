package com.example.snapcampus.controller.api;

import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.dto.request.post.PostLikeDtoRequest;
import com.example.snapcampus.dto.response.post.PostLikeDtoResponse;
import com.example.snapcampus.service.PostLikeService;
import com.example.snapcampus.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/post/like")
public class PostLikeApiController {
    private final PostLikeService postLikeService;
    private final SecurityService securityService;

    public PostLikeApiController(PostLikeService postLikeService, SecurityService securityService) {
        this.postLikeService = postLikeService;
        this.securityService = securityService;
    }

    @PostMapping("/update")
    public BaseResponse<PostLikeDtoResponse> update(@RequestBody @Valid PostLikeDtoRequest postLikeDtoRequest){
        securityService.checkIsLogined();

        String memberUserId = securityService.getMemberUserId();
        PostLikeDtoResponse response = postLikeService.update(memberUserId, postLikeDtoRequest);
        return new BaseResponse<>(response);
    }
}
