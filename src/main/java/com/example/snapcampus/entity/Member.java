package com.example.snapcampus.entity;


import com.example.snapcampus.dto.response.member.MemberDtoResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@ToString(callSuper = true) // auditing 필드까지 호출
@Table(indexes = {
        @Index(columnList = "userId", unique = true),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "nick", unique = true),
        @Index(columnList = "createdBy")
})
@Entity
public class Member extends AuditingFields {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nick;

    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String department;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.ALL, targetEntity = MemberRole.class)
    private List<MemberRole> memberRole = new ArrayList<>();

    protected Member() {}

    public Member(String userId, String password, String nick, String name, String email, String department) {
        this.userId = userId;
        this.password = password;
        this.nick = nick;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public MemberDtoResponse toDto() {
        return new MemberDtoResponse(id, userId, nick, name, department, email);
    }

    // TODO: of, equals, hashcode METHODS

//    public static Member of(String userId, String password, String nick, String name, String email, String department) {
//        return Member.of(userId, password, nick, name, email, department, null);
//    }
//
//    public static Member of(String userId, String password, String nick, String name, String email, String department, String createdBy) {
//        return new Member(userId, password, nick, name, email, department, createdBy);
//    }

//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof UserAccount that)) return false;
//        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(this.getUserId());
//    }

}
