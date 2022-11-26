package hello.tumblbug.repository;

import hello.tumblbug.domain.MemberProject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
@RequiredArgsConstructor
public class MemberProjectRepository {

    private final EntityManager em;

    public Long save(MemberProject memberProject) {
        em.persist(memberProject);
        return memberProject.getId();
    }
}
