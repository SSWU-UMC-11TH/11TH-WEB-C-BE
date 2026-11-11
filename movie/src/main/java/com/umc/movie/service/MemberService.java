package com.umc.movie.service;

import com.umc.movie.domain.Rating;
import com.umc.movie.domain.Member;
import com.umc.movie.dto.EmailCheckResponse;
import com.umc.movie.dto.MemberResponse;
import com.umc.movie.dto.NicknameCheckResponse;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.dto.SignUpRequest;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.umc.movie.dto.MemberInfoResponse;
import com.umc.movie.exception.ApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final RatingRepository ratingRepository;
    private final PasswordEncoder passwordEncoder;

    public NicknameCheckResponse checkNickname(String nickname) {

        boolean exists = memberRepository.existsByNickname(nickname);

        return new NicknameCheckResponse(!exists);
    }

    public EmailCheckResponse checkEmail(String email) {

        boolean exists = memberRepository.existsByEmail(email);

        return new EmailCheckResponse(!exists);
    }

    // 회원가입
    @Transactional
    public MemberResponse signUp(SignUpRequest request) {

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임 중복 확인
        if (memberRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 회원 객체 생성
        Member member = Member.builder()
                .email(request.email())
                .password(encodedPassword)
                .nickname(request.nickname())
                .build();

        // DB 저장
        Member savedMember = memberRepository.save(member);

        // 응답 반환
        return new MemberResponse(
                savedMember.getMemberId(),
                savedMember.getEmail(),
                savedMember.getNickname()
        );
    }

    public List<RatingResponse> getRatings(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 회원입니다."
            );
        }

        List<Rating> ratings =
                ratingRepository.findByMember_MemberIdOrderByRatingIdDesc(memberId);

        return ratings.stream()
                .map(rating -> RatingResponse.from(rating))
                .toList();
    }


    // 내 정보 조회
    public MemberInfoResponse getMyInfo(Long memberId) {

        // 회원 ID로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "MEMBER404_1",
                        "해당 회원을 찾을 수 없습니다."
                ));

        // 회원 정보를 DTO로 변환하여 반환
        return MemberInfoResponse.from(member);
    }

}