package com.example.snapcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
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
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", foreignKey = @ForeignKey(name = "fk_post_map_id"))
    private Map map;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", foreignKey = @ForeignKey(name = "fk_post_event_id"))
    private Event event;

    @Column(nullable = false)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> image;

    @Column(nullable = false)
    private String likeCount;

    @Column(nullable = false)
    private String department;
}
