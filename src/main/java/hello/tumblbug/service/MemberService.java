package hello.tumblbug.service;

import hello.tumblbug.domain.Member;
import hello.tumblbug.file.UploadFile;
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

    @Transactional(readOnly = true)
    public Member findOne(Long id) {
        return memberRepository.findById(id);
    }

    public Member updateMember(Long id, UploadFile userImage, String username, String password, String info) {
        Member member = memberRepository.findById(id);
        member.update(userImage, username, password, info);
        return member;
    }

    public int followOrStopFollowingMember(Long followerId, Long followeeId) {
        Member follower = memberRepository.findById(followerId);
        Member followee = memberRepository.findById(followeeId);
        if (follower.getFollowings().contains(followee)) {
            follower.getFollowings().remove(followee);
            return 0;
        }
        follower.getFollowings().add(followee);
        return 1;
        //return 1 if follower follows followee else return 0
    }

    @Transactional(readOnly = true)
    public boolean follows(Long followerId, Long followeeId) {
        Member follower = memberRepository.findById(followerId);
        Member followee = memberRepository.findById(followeeId);
        return follower.getFollowings().contains(followee);
    }
}
