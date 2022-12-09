package hello.tumblbug.repository;

import hello.tumblbug.domain.CommunityPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
@RequiredArgsConstructor
public class CommunityPostRepository {

    private final EntityManager em;

    public Long save(CommunityPost communityPost) {
        em.persist(communityPost);
        return communityPost.getId();
    }
}
