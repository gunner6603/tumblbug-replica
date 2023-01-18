package hello.tumblbug.service;

import hello.tumblbug.domain.Member;
import hello.tumblbug.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member login(String loginId, String password) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isEmpty() || !member.get().getPassword().equals(password)) {
            return null;
        }
        return member.get();
    }
}
