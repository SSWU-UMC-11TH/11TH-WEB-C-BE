package com.umc.movie.repository;

import com.umc.movie.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);

    boolean existsByNicknameAndMemberIdNot(String nickname, Long memberId);
}