package com.example.snapcampus.controller.api;


import com.example.snapcampus.common.dto.BaseResponse;
import com.example.snapcampus.dto.request.comment.CommentAddDtoRequest;
import com.example.snapcampus.dto.request.comment.CommentDeleteDtoRequest;
import com.example.snapcampus.dto.request.comment.CommentUpdateDtoRequest;
import com.example.snapcampus.service.CommentService;
import com.example.snapcampus.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comment")
public class CommentApiController {
    private final CommentService commentService;
    private final SecurityService securityService;

    public CommentApiController(CommentService commentService, SecurityService securityService) {
        this.commentService = commentService;
        this.securityService = securityService;
    }

    @PostMapping("/add")
    public BaseResponse<Object> add(@RequestBody @Valid CommentAddDtoRequest commentAddDtoRequest){
        securityService.checkIsLogined();

        String resultMsg = commentService.add(securityService.getMemberUserId(), commentAddDtoRequest);
        return new BaseResponse<>(resultMsg);
    }

    @PostMapping("/update")
    public BaseResponse<Object> update(@RequestBody @Valid CommentUpdateDtoRequest commentUpdateDtoRequest){
        securityService.checkIsLogined();

        String resultMsg = commentService.update(securityService.getMemberUserId(), commentUpdateDtoRequest);
        return new BaseResponse<>(resultMsg);
    }

    @PostMapping("/delete")
    public BaseResponse<Object> delete(@RequestBody @Valid CommentDeleteDtoRequest commentDeleteDtoRequest){
        securityService.checkIsLogined();

        String resultMsg = commentService.delete(securityService.getMemberUserId(), securityService.checkHasAdmin(), commentDeleteDtoRequest);
        return new BaseResponse<>(resultMsg);
    }
}
