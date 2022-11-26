package hello.tumblbug.repository;

import hello.tumblbug.domain.Project;
import hello.tumblbug.dto.SimpleProjectDto;
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

    public List<SimpleProjectDto> findAllAsSimpleProjectDto() {
        return em.createQuery("select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) from Project p join p.creator m", SimpleProjectDto.class)
                .getResultList();
    }

    public List<SimpleProjectDto> findAllSimpleByTimeDescWithOffsetLimit(int offset, int limit) {
        return em.createQuery(
                "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                        "from Project p join p.creator m " +
                        "order by p.createdTime desc", SimpleProjectDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<SimpleProjectDto> findAllSimpleByCurrentSponsorshipDescWithOffsetLimit(int offset, int limit) {
        return em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "order by p.currentSponsorship desc", SimpleProjectDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<SimpleProjectDto> findAllSimpleByCreatorId(Long memberId) {
        return em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "where m.id = :memberId " +
                                "order by p.createdTime desc", SimpleProjectDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
