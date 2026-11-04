package com.umc.movie.service;

import com.umc.movie.domain.Rating;
import com.umc.movie.dto.EmailCheckResponse;
import com.umc.movie.dto.NicknameCheckResponse;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final RatingRepository ratingRepository;

    public NicknameCheckResponse checkNickname(String nickname) {

        boolean exists = memberRepository.existsByNickname(nickname);

        return new NicknameCheckResponse(!exists);
    }

    public EmailCheckResponse checkEmail(String email) {

        boolean exists = memberRepository.existsByEmail(email);

        return new EmailCheckResponse(!exists);
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
}