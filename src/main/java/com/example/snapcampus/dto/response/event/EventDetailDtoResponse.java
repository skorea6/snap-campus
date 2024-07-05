package com.example.snapcampus.dto.response.event;

import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import com.example.snapcampus.dto.response.post.PostDetailDtoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventDetailDtoResponse extends EventDtoResponse {
    private List<PostDetailDtoResponse> posts;
    private List<List<PostDetailDtoResponse>> columnPosts = new ArrayList<>(4); // 4개의 칼럼을 위한 배열

    public EventDetailDtoResponse(Long id, String name, String description, String startDate, String stopDate, String organizer, MemberDtoResponse member, List<PostDetailDtoResponse> posts) {
        super(id, name, description, startDate, stopDate, organizer, member);
        this.posts = posts;

        for (int i = 0; i < 4; i++) {
            columnPosts.add(new ArrayList<>());
        }

        processEventPosts(posts, columnPosts); // 각 이벤트를 처리
    }

    public static void processEventPosts(List<PostDetailDtoResponse> posts, List<List<PostDetailDtoResponse>> columnPosts) {
        posts.forEach(post -> {
            if (post.getColumnIndex() != null && post.getColumnIndex() >= 1 && post.getColumnIndex() <= 4) {
                columnPosts.get((int) (post.getColumnIndex() - 1)).add(post);
            }
        });
    }
}
