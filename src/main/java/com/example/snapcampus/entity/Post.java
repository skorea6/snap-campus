package com.example.snapcampus.entity;

import com.example.snapcampus.dto.response.post.PostAggregateDtoResponse;
import com.example.snapcampus.dto.response.post.PostDetailDtoResponse;
import com.example.snapcampus.util.DateUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@ToString(callSuper = true) // auditing 필드까지 호출
@Table(indexes = {
        @Index(columnList = "createdBy")
})
@Entity
public class Post extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Post 여러개 하나의 회원 Member
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_post_member_id"))
    @ToString.Exclude
    @Setter
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", foreignKey = @ForeignKey(name = "fk_post_map_id"))
    @ToString.Exclude
    @Setter
    private Map map;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", foreignKey = @ForeignKey(name = "fk_post_event_id"))
    @ToString.Exclude
    @Setter
    private Event event;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @Setter
    private List<String> images;

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(nullable = false)
    private String department;

    public Post(String title, String description, String department) {
        this.title = title;
        this.description = description;
        this.department = department;
    }

    public Post() {

    }

    public PostDetailDtoResponse toDetailDto(Long columnIndex){
        return new PostDetailDtoResponse(id, title, description, likeCount, department, DateUtil.formatDateTime(createdAt), images.get(0), images, columnIndex, member.toDto());
    }

    public PostAggregateDtoResponse toAggregateDto(){
        return new PostAggregateDtoResponse(id, title, description, likeCount, department, DateUtil.formatDateTime(createdAt), images.get(0), images, member.toDto(), event.toAggregateDto());
    }
}
