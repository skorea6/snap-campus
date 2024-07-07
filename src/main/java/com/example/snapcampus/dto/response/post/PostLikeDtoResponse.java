package com.example.snapcampus.dto.response.post;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikeDtoResponse {
    private Long likeCount;
    private Boolean status;

    public PostLikeDtoResponse(Long likeCount, Boolean status) {
        this.likeCount = likeCount;
        this.status = status;
    }
}
