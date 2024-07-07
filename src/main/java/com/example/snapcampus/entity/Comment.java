package com.example.snapcampus.entity;


import com.example.snapcampus.dto.response.comment.CommentDtoResponse;
import com.example.snapcampus.util.DateUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true) // auditing 필드까지 호출
@Table(indexes = {
        @Index(columnList = "createdBy")
})
@Entity
public class Comment extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_comment_member_id"))
    @ToString.Exclude
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_comment_post_id"))
    @ToString.Exclude
    private Post post;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column
    private String deletedText;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false)
    private Boolean isUpdated = false;

    public Comment() {
    }

    public Comment(Member member, Post post, String content) {
        this.member = member;
        this.post = post;
        this.content = content;
    }

    public void update(String content){
        this.isUpdated = true;
        this.content = content;
    }

    public void delete(String deletedMessage){
        this.isDeleted = true;
        this.deletedText = content;
        this.content = deletedMessage;
    }

    public CommentDtoResponse toDto(){
        return new CommentDtoResponse(id, member.toDto(), content, isUpdated, isDeleted, DateUtil.formatDateTimeMonthAndDate(createdAt), DateUtil.formatDateTime(createdAt));
    }
}

