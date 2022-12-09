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

    public CommunityPost findById(Long id) {
        return em.find(CommunityPost.class, id);
    }

    public void deleteById(Long id) {
        CommunityPost communityPost = findById(id);
        em.remove(communityPost);
    }
}
