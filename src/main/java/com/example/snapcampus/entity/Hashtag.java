package com.example.snapcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true) // auditing 필드까지 호출
@Table(indexes = {
        @Index(columnList = "tag"),
        @Index(columnList = "createdBy"),
})
@Entity
public class Hashtag extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_hashtag_post_id"))
    private Post post;

    @Column(nullable = false)
    private String tag;
}

