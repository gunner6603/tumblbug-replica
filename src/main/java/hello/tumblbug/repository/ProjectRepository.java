package hello.tumblbug.repository;

import hello.tumblbug.domain.Project;
import hello.tumblbug.dto.SimpleProjectDto;
import hello.tumblbug.dto.SimpleProjectDtoWithTotal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
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

    public SimpleProjectDtoWithTotal findAllSimpleByTimeDescWithOffsetLimit(int offset, int limit, boolean needTotal) {
        SimpleProjectDtoWithTotal simpleProjectDtoWithTotal = new SimpleProjectDtoWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "order by p.createdTime desc", SimpleProjectDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtoWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery("select count(p) from Project p", Long.class)
                    .getSingleResult();
            simpleProjectDtoWithTotal.setTotal(total);
        }
        return simpleProjectDtoWithTotal;
    }

    public SimpleProjectDtoWithTotal findAllNotExpiredSimpleByDeadlineAscWithOffsetLimit(int offset, int limit, boolean needTotal) {
        SimpleProjectDtoWithTotal simpleProjectDtoWithTotal = new SimpleProjectDtoWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "where p.deadline > :currentTime " +
                                "order by p.deadline", SimpleProjectDto.class)
                .setParameter("currentTime", LocalDateTime.now())
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtoWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery(
                    "select count(p) from Project p " +
                            "where p.deadline > :currentTime", Long.class)
                    .setParameter("currentTime", LocalDateTime.now())
                    .getSingleResult();
            simpleProjectDtoWithTotal.setTotal(total);
        }
        return simpleProjectDtoWithTotal;
    }

    public SimpleProjectDtoWithTotal findAllSimpleByCurrentSponsorshipDescWithOffsetLimit(int offset, int limit, boolean needTotal) {
        SimpleProjectDtoWithTotal simpleProjectDtoWithTotal = new SimpleProjectDtoWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "order by p.currentSponsorship desc", SimpleProjectDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtoWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery("select count(p) from Project p", Long.class)
                    .getSingleResult();
            simpleProjectDtoWithTotal.setTotal(total);
        }
        return simpleProjectDtoWithTotal;
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

    public List<SimpleProjectDto> findAllSimpleBySponsorId(Long memberId) {
        return em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Member sp " +
                                "join sp.memberProjects mp " +
                                "join mp.project p " +
                                "join p.creator m " +
                                "where sp.id = :memberId " +
                                "order by mp.sponsoredTime desc", SimpleProjectDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
