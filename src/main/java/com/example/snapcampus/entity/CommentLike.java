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
public class CommentLike extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_comment_like_member_id"))
    @ToString.Exclude
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "fk_comment_like_comment_id"))
    @ToString.Exclude
    private Comment comment;
}

