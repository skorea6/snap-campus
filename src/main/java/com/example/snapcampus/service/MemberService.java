package com.example.snapcampus.service;

import com.example.snapcampus.common.login.TokenInfo;
import com.example.snapcampus.common.login.jwt.JwtTokenProvider;
import com.example.snapcampus.common.status.RoleType;
import com.example.snapcampus.dto.request.member.LoginDto;
import com.example.snapcampus.dto.request.member.MemberSignUpDtoRequest;
import com.example.snapcampus.dto.request.member.SignUpVerificationSendEmailDtoRequest;
import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.MemberRole;
import com.example.snapcampus.redis.dto.EmailVerificationDto;
import com.example.snapcampus.redis.dto.EmailVerificationDtoResponse;
import com.example.snapcampus.redis.repository.RefreshTokenInfoRepositoryRedis;
import com.example.snapcampus.redis.repository.EmailVerificationRepositoryRedis;
import com.example.snapcampus.repository.MemberRepository;
import com.example.snapcampus.repository.MemberRoleRepository;
import com.example.snapcampus.util.MailUtil;
import com.example.snapcampus.util.RandomUtil;
import com.example.snapcampus.util.SenderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final EmailVerificationRepositoryRedis emailVerificationRepositoryRedis;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenInfoRepositoryRedis refreshTokenInfoRepositoryRedis;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public Optional<Member> searchUser(String username){
        return Optional.ofNullable(memberRepository.findByUserId(username)); // .map(MemberDto::from)
    }

    /**
     * 로그인 -> 토큰 발행
     */
    public TokenInfo login(LoginDto loginDto) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        TokenInfo createToken = jwtTokenProvider.createToken(authentication);

        refreshTokenInfoRepositoryRedis.save(loginDto.getUserId(), createToken.getRefreshToken());
        return createToken;
    }

    /**
     * 유저의 모든 Refresh 토큰 삭제
     */
    public void deleteAllRefreshToken(String userId) {
        refreshTokenInfoRepositoryRedis.deleteByUserId(userId);
    }

    /**
     * Refresh 토큰 검증 후 토큰 재발급
     */
    public TokenInfo validateRefreshTokenAndCreateToken(String refreshToken) throws IllegalArgumentException {
        // Redis에 refreshToken 유효 여부 확인
        if (refreshTokenInfoRepositoryRedis.findByRefreshToken(refreshToken) == null) {
            throw new IllegalArgumentException("refreshToken expired or not found. Re-login required.");
        }

        // 새로운 accessToken, refreshToken 발급
        TokenInfo newTokenInfo = jwtTokenProvider.validateRefreshTokenAndCreateToken(refreshToken);

        // 기존 refreshToken Redis에서 제거 : refreshToken은 1회용
        refreshTokenInfoRepositoryRedis.deleteByRefreshToken(refreshToken);

        // 새로운 refreshToken Redis에 추가
        refreshTokenInfoRepositoryRedis.save(newTokenInfo.getUserId(), newTokenInfo.getRefreshToken());

        return newTokenInfo;
    }

    /**
     * 회원가입
     */
    public void signUp(MemberSignUpDtoRequest request, String verificationToken) {
        EmailVerificationDto emailVerificationDto = validateEmailVerification("SignUp", verificationToken);

        checkDuplicateUserId(request.getUserId()); // userId 중복 검사
        checkDuplicateNick(request.getNick()); // nick 중복 검사

        Member member = request.toEntity(emailVerificationDto.getEmail());
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member);
        memberRoleRepository.save(new MemberRole(RoleType.MEMBER, member));

        completeEmailVerification("SignUp", verificationToken); // 이메일 인증 토큰 제거
    }

    /**
     * 회원가입 - 이메일 인증번호 발송
     */
    public EmailVerificationDtoResponse signUpVerificationSendEmail(SignUpVerificationSendEmailDtoRequest signUpVerificationSendEmailDtoRequest){
        checkDuplicateEmail(signUpVerificationSendEmailDtoRequest.getEmail()); // email 중복 검사

        return verificationSendEmail(
                "SignUp",
                signUpVerificationSendEmailDtoRequest.getEmail(),
                "회원가입 인증번호 안내",
                "회원가입을 위해서 아래 인증코드를 입력해주세요."
        );
    }

    /**
     * 회원가입 - 이메일 인증번호 확인
     */
    public EmailVerificationDto signUpVerificationCheckEmail(String token, String verificationNumber){
        EmailVerificationDto emailVerificationDto = verificationCheckEmail("SignUp", token, verificationNumber);
        emailVerificationRepositoryRedis.save("SignUp", emailVerificationDto, 15); // 10분 타임아웃 제한
        return emailVerificationDto;
    }

    private void checkDuplicateEmail(String email) {
        Member findMember = memberRepository.findByEmail(email);
        if (findMember != null) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
    }

    private void checkDuplicateUserId(String userId) {
        Member findMember = memberRepository.findByUserId(userId);
        if (findMember != null) {
            throw new IllegalArgumentException("이미 등록된 아이디입니다.");
        }
    }

    private void checkDuplicateNick(String nick) {
        Member findMember = memberRepository.findByNick(nick);
        if (findMember != null) {
            throw new IllegalArgumentException("이미 등록된 닉네임입니다.");
        }
    }

    public EmailVerificationDtoResponse verificationSendEmail(
            String name,
            String emailAddress,
            String emailSubject,
            String emailContent
    ) {
        String verificationToken = RandomUtil.generateRandomString(32);
        String verificationNumber = RandomUtil.generateRandomNumber(6);

        EmailVerificationDto emailVerificationDto = new EmailVerificationDto(
                emailAddress,
                verificationToken,
                verificationNumber
        );
        emailVerificationRepositoryRedis.save(name, emailVerificationDto, 30); // 30분 타임아웃 제한

        MailUtil.send(new SenderDto(
                new ArrayList<>(Collections.singletonList(emailAddress)),
                "스냅캠퍼스 - " + emailSubject,
                "안녕하세요, 스냅캠퍼스입니다.<br><br>" + emailContent + "<br>인증번호는 <b>" + verificationNumber + "</b> 입니다."
        ));

        return emailVerificationDto.toResponse();
    }

    private EmailVerificationDto verificationCheckEmail(String name, String token, String verificationNumber) {
        EmailVerificationDto emailVerificationDto = emailVerificationRepositoryRedis.findByVerificationToken(
                name,
                token
        );

        if (emailVerificationDto == null) {
            throw new IllegalArgumentException("이메일 인증 시간이 초과되었습니다. 재시도해주세요.");
        }

        if (emailVerificationDto.isDone()) {
            throw new IllegalArgumentException("이미 이메일 인증이 완료된 상태입니다.");
        }

        if (emailVerificationDto.getAttemptCount() > 10) {
            throw new IllegalArgumentException("너무 많은 시도를 하였습니다. 처음부터 재시도해주세요.");
        }

        if (!emailVerificationDto.getVerificationNumber().equals(verificationNumber)) {
            emailVerificationDto.setAttemptCount(emailVerificationDto.getAttemptCount() + 1);
            emailVerificationRepositoryRedis.save(name, emailVerificationDto, 30);
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }

        emailVerificationDto.setDone(true);
        return emailVerificationDto;
    }

    private EmailVerificationDto validateEmailVerification(String name, String token) {
        EmailVerificationDto emailVerificationDto = emailVerificationRepositoryRedis.findByVerificationToken(
            name,
            token
        );

        if(emailVerificationDto == null){
            throw new IllegalArgumentException("시간이 초과되었습니다. 재시도해주세요.");
        }

        if (!emailVerificationDto.isDone()) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        return emailVerificationDto;
    }

    private void completeEmailVerification(String name, String token){
        emailVerificationRepositoryRedis.deleteByVerificationToken(
            name,
            token
        );
    }
}
