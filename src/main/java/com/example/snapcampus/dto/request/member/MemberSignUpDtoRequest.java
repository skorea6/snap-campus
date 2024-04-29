package com.example.snapcampus.dto.request.member;


import com.example.snapcampus.entity.Member;
import com.example.snapcampus.util.PatternUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberSignUpDtoRequest {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = PatternUtil.USER_ID_PATTERN, message = PatternUtil.USER_ID_MESSAGE)
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = PatternUtil.PASSWORD_PATTERN, message = PatternUtil.PASSWORD_MESSAGE)
    private String password;

    private String passwordRe;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = PatternUtil.NICK_PATTERN, message = PatternUtil.NICK_MESSAGE)
    private String nick;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = PatternUtil.NAME_PATTERN, message = PatternUtil.NAME_MESSAGE)
    private String name;

    @NotBlank(message = "학과는 필수 입력 값입니다.")
    private String department;

    private String email;

    public Member toEntity(String email) {
        return new Member(userId, password, nick, name, email, department);
    }
}
