package com.example.snapcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true) // auditing 필드까지 호출
@Table(indexes = {
        @Index(columnList = "createdBy")
})
@Entity
public class PostLike extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_post_like_member_id"))
    @ToString.Exclude
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_post_like_post_id"))
    @ToString.Exclude
    private Post post;

    public PostLike() {
    }

    public PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}
