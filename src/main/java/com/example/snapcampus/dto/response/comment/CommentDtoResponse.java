package com.example.snapcampus.dto.response.comment;


import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDtoResponse {
    private Long id;
    private MemberDtoResponse member;
    private String content;
    private Boolean isUpdated;
    private Boolean isDeleted;
    private String createdAtShort;
    private String createdAt;

    public CommentDtoResponse(Long id, MemberDtoResponse member, String content, Boolean isUpdated, Boolean isDeleted, String createdAtShort, String createdAt) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
        this.createdAtShort = createdAtShort;
        this.createdAt = createdAt;
    }
}
