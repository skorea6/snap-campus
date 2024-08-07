package com.example.snapcampus.dto.response.member;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDtoResponse {
    private Long id;
    private String userId;
    private String nick;
    private String name;
    private String department;
    private String email;

    public MemberDtoResponse(Long id, String userId, String nick, String name, String department, String email) {
        this.id = id;
        this.userId = userId;
        this.nick = nick;
        this.name = name;
        this.department = department;
        this.email = email;
    }
}
