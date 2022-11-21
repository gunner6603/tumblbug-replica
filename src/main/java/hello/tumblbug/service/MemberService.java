package hello.tumblbug.service;

import hello.tumblbug.domain.Member;
import hello.tumblbug.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(String username, String loginId, String password) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        if (!findMember.isEmpty()) {
            return null;
        }
        Member member = new Member(username, loginId, password);
        memberRepository.save(member);
        return member.getId();
    }
}
