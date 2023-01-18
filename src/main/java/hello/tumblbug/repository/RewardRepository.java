package hello.tumblbug.repository;

import hello.tumblbug.domain.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RewardRepository {

    private final EntityManager em;

    public Reward findById(Long id) {
        return em.find(Reward.class, id);
    }
}
