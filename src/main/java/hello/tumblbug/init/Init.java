package hello.tumblbug.init;

import hello.tumblbug.domain.Member;
import hello.tumblbug.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
@RequiredArgsConstructor
public class Init {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member member = new Member("test", "test", "test");
        memberRepository.save(member);
    }
}
