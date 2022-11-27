package hello.tumblbug.service;

import hello.tumblbug.domain.Member;
import hello.tumblbug.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
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

    public Member findOne(Long id) {
        return memberRepository.findById(id);
    }

    public Member updateMember(Long id, String username, String password, String info) {
        Member member = memberRepository.findById(id);
        member.setUsername(username);
        member.setPassword(password);
        member.setInfo(info);
        return member;
    }

    public void followMember(Long followerId, Long followeeId) {
        Member follower = memberRepository.findById(followerId);
        Member followee = memberRepository.findById(followeeId);
        follower.getFollowings().add(followee);
    }
}
