package com.example.snapcampus.dto.security;

import com.example.snapcampus.common.status.RoleType;
import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record SnapPrincipal(
        String userId,
        String password,
        String nick,
        String name,
        String email,
        String department,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

//    public static SnapPrincipal of(String userId, String password, String nick, String name, String email, String department) {
//        // 지금은 인증만 하고 권한을 다루고 있지 않아서 임의로 세팅한다.
//        return SnapPrincipal.of(userId, password, nick, name, email, department, Map.of());
//    }

//    public static SnapPrincipal of(String userId){
//        return SnapPrincipal.of(userId);
//    }

    public SnapPrincipal(String userId, Collection<? extends GrantedAuthority> authorities) {
        this(userId, null, null, null, null, null, authorities); // 기본 생성자에 위임
    }

    public static SnapPrincipal of(String userId, String password, String nick, String name, String email, String department, Set<RoleType> roleTypes) {
        //Set<RoleType> roleTypes = Set.of(RoleType.MEMBER);

        return new SnapPrincipal(
                userId,
                password,
                nick,
                name,
                email,
                department,
                roleTypes.stream()
                        .map(RoleType::name)
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    public static SnapPrincipal from(Member member) {
        return SnapPrincipal.of(
                member.getUserId(),
                member.getPassword(),
                member.getNick(),
                member.getName(),
                member.getEmail(),
                member.getDepartment(),
                member.getMemberRole().stream().map(MemberRole::getRoleType).collect(Collectors.toSet())
        );
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public String nick() {
        return nick;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String department() {
        return department;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
