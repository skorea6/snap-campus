package com.example.snapcampus.dto.response.event;

import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import com.example.snapcampus.dto.response.post.PostDtoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventDtoResponse {
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String stopDate;
    private String organizer;

    private MemberDtoResponse member;
    private List<PostDtoResponse> posts;
    private List<List<PostDtoResponse>> columnPosts = new ArrayList<>(4); // 4개의 칼럼을 위한 배열

    public EventDtoResponse() {
        for (int i = 0; i < 4; i++) {
            columnPosts.add(new ArrayList<>());
        }
    }

    public EventDtoResponse(Long id, String name, String description, String startDate, String stopDate, String organizer, MemberDtoResponse member, List<PostDtoResponse> posts) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.organizer = organizer;
        this.member = member;
        this.posts = posts;

        processEventPosts(posts, columnPosts); // 각 이벤트를 처리
    }

    public static void processEventPosts(List<PostDtoResponse> posts, List<List<PostDtoResponse>> columnPosts) {
        posts.forEach(post -> {
            if (post.getColumnIndex() != null && post.getColumnIndex() >= 1 && post.getColumnIndex() <= 4) {
                columnPosts.get((int) (post.getColumnIndex() - 1)).add(post);
            }
        });
    }
}
