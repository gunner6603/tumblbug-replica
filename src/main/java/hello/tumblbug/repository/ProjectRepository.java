package hello.tumblbug.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.tumblbug.domain.Category;
import hello.tumblbug.domain.Project;
import hello.tumblbug.domain.QMember;
import hello.tumblbug.domain.QProject;
import hello.tumblbug.dto.QSimpleProjectDto;
import hello.tumblbug.dto.SimpleProjectDto;
import hello.tumblbug.dto.SimpleProjectDtosWithTotal;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Repository
@Transactional
public class ProjectRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ProjectRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

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

    public SimpleProjectDtosWithTotal findSimpleByTimeDescWithOffsetLimit(int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "order by p.createdTime desc", SimpleProjectDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtosWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery("select count(p) from Project p", Long.class)
                    .getSingleResult();
            simpleProjectDtosWithTotal.setTotal(total);
        }
        return simpleProjectDtosWithTotal;
    }

    public SimpleProjectDtosWithTotal findNotExpiredSimpleByDeadlineAscWithOffsetLimit(int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "where p.deadline > :currentTime " +
                                "order by p.deadline", SimpleProjectDto.class)
                .setParameter("currentTime", LocalDateTime.now())
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtosWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery(
                    "select count(p) from Project p " +
                            "where p.deadline > :currentTime", Long.class)
                    .setParameter("currentTime", LocalDateTime.now())
                    .getSingleResult();
            simpleProjectDtosWithTotal.setTotal(total);
        }
        return simpleProjectDtosWithTotal;
    }

    public SimpleProjectDtosWithTotal findSimpleByCurrentSponsorshipDescWithOffsetLimit(int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "order by p.currentSponsorship desc", SimpleProjectDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtosWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery("select count(p) from Project p", Long.class)
                    .getSingleResult();
            simpleProjectDtosWithTotal.setTotal(total);
        }
        return simpleProjectDtosWithTotal;
    }

    public SimpleProjectDtosWithTotal findSimpleByCategoryAndTimeDescWithOffsetLimit(Category category, int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "where p.category = :category " +
                                "order by p.createdTime desc", SimpleProjectDto.class)
                .setParameter("category", category)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtosWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery(
                    "select count(p) from Project p " +
                            "where p.category = :category", Long.class)
                    .setParameter("category", category)
                    .getSingleResult();
            simpleProjectDtosWithTotal.setTotal(total);
        }
        return simpleProjectDtosWithTotal;
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

    public SimpleProjectDtosWithTotal findSimpleByQueryStringOnProjectTitleAndCreatorUsernameWithOffsetLimit(String query, int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        QProject p = QProject.project;
        QMember m = QMember.member;
        List<SimpleProjectDto> resultList = queryFactory
                .select(new QSimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship))
                .from(p)
                .join(p.creator, m)
                .where(orCondition(projectTitleContains(query), memberUsernameContains(query)))
                .orderBy(p.createdTime.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
        simpleProjectDtosWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = queryFactory
                    .select(p.count())
                    .from(p)
                    .join(p.creator, m)
                    .where(orCondition(projectTitleContains(query), memberUsernameContains(query)))
                    .fetchOne();
            simpleProjectDtosWithTotal.setTotal(total);
        }
        return simpleProjectDtosWithTotal;
    }

    private BooleanExpression projectTitleContains(String query) {
        QProject p = QProject.project;
        return hasText(query) ? p.title.contains(query) : null;
    }

    private BooleanExpression memberUsernameContains(String query) {
        QMember m = QMember.member;
        return hasText(query) ? m.username.contains(query) : null;
    }

    private BooleanExpression orCondition(BooleanExpression e1, BooleanExpression e2) {
        if (e1 == null) {
            return e2;
        }
        return e1.or(e2);
    }

}
