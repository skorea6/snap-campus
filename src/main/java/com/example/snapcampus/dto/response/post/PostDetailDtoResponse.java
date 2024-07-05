package com.example.snapcampus.dto.response.post;


import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDetailDtoResponse extends PostDtoResponse {

    private Long columnIndex;

    public PostDetailDtoResponse(Long id, String title, String description, Long likeCount, String department, String createdAt, String thumbImage, List<String> images, Long columnIndex, MemberDtoResponse member) {
        super(id, title, description, likeCount, department, createdAt, thumbImage, images, member);
        this.columnIndex = columnIndex;
    }
}
