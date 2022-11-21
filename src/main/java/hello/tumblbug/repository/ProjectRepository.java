package hello.tumblbug.repository;

import hello.tumblbug.domain.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ProjectRepository {

    private final EntityManager em;

    public Long save(Project project) {
        em.persist(project);
        return project.getId();
    }

    public Project findById(Long id) {
        return em.find(Project.class, id);
    }

    public List<Project> findAll() {
        return em.createQuery("select p from Project p", Project.class)
                .getResultList();
    }
}
