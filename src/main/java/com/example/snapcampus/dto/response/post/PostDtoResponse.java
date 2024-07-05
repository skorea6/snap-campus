package com.example.snapcampus.dto.response.post;


import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDtoResponse {
    private Long id;
    private String title;
    private String description;
    private Long likeCount;
    private String department;
    private String createdAt;
    private String thumbImage;
    private List<String> images;

    private MemberDtoResponse member;

    public PostDtoResponse(Long id, String title, String description, Long likeCount, String department, String createdAt, String thumbImage, List<String> images, MemberDtoResponse member) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.likeCount = likeCount;
        this.department = department;
        this.createdAt = createdAt;
        this.thumbImage = thumbImage;
        this.images = images;
        this.member = member;
    }
}
