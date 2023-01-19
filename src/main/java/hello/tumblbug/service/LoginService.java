package hello.tumblbug.service;

import hello.tumblbug.domain.Member;
import hello.tumblbug.domain.encryption.PasswordEncrypt;
import hello.tumblbug.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member login(String loginId, String loginPassword) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isEmpty()) {
            return null;
        }
        String memberPassword = member.get().getPassword();
        String memberSalt = member.get().getSalt();
        try {
            if (!PasswordEncrypt.encrypt(loginPassword, memberSalt).equals(memberPassword)) {
                return null;
            }
            return member.get();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
