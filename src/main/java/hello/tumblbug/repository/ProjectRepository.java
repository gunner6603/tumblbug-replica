package hello.tumblbug.repository;

//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.tumblbug.domain.Category;
import hello.tumblbug.domain.Project;
//import hello.tumblbug.domain.QMember;
//import hello.tumblbug.domain.QProject;
//import hello.tumblbug.dto.QSimpleProjectDto;
import hello.tumblbug.dto.SimpleProjectDto;
import hello.tumblbug.dto.SimpleProjectDtosWithTotal;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProjectRepository {

    private final EntityManager em;
    //private final JPAQueryFactory queryFactory;

    public ProjectRepository(EntityManager em) {
        this.em = em;
        //this.queryFactory = new JPAQueryFactory(em);
    }

    public Long save(Project project) {
        em.persist(project);
        return project.getId();
    }

    public Project findById(Long id) {
        return em.find(Project.class, id);
    }

    //프로젝트 상세 화면 출력에 사용(프로젝트 정보와 창작자, 리워드 정보가 함께 필요한 경우)
    public Project findByIdFetchCreatorAndReward(Long id) {
        return em.createQuery("select distinct p from Project p " +
                "join fetch p.creator " +
                "join fetch p.rewards " +
                "where p.id = :projectId", Project.class)
                .setParameter("projectId", id)
                .getSingleResult();
    }

    //신규 프로젝트 목록 출력에 사용
    public SimpleProjectDtosWithTotal findSimpleByTimeDescWithOffsetLimit(int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "order by p.dateCreated desc", SimpleProjectDto.class)
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

    //마감임박 프로젝트 목록 출력에 사용
    public SimpleProjectDtosWithTotal findNotExpiredSimpleByDeadlineAscWithOffsetLimit(int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "where p.deadline > :currentTime " +
                                "order by p.deadline", SimpleProjectDto.class)
                .setParameter("currentTime", currentTime)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtosWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery(
                    "select count(p) from Project p " +
                            "where p.deadline > :currentTime", Long.class)
                    .setParameter("currentTime", currentTime)
                    .getSingleResult();
            simpleProjectDtosWithTotal.setTotal(total);
        }
        return simpleProjectDtosWithTotal;
    }

    //인기 프로젝트 목록 출력에 사용
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

    //카테고리별 프로젝트 목록 출력에 사용
    public SimpleProjectDtosWithTotal findSimpleByCategoryAndTimeDescWithOffsetLimit(Category category, int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "where p.category = :category " +
                                "order by p.dateCreated desc", SimpleProjectDto.class)
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

    //올린 프로젝트 목록 출력에 사용
    public List<SimpleProjectDto> findAllSimpleByCreatorId(Long memberId) {
        return em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "where m.id = :memberId " +
                                "order by p.dateCreated desc", SimpleProjectDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    //후원한 프로젝트 목록 출력에 사용
    public List<SimpleProjectDto> findAllSimpleBySponsorId(Long memberId) {
        return em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Member sp " +
                                "join sp.memberProjects mp " +
                                "join mp.project p " +
                                "join p.creator m " +
                                "where sp.id = :memberId " +
                                "order by mp.dateCreated desc", SimpleProjectDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    //검색에 사용
    public SimpleProjectDtosWithTotal findSimpleByQueryStringOnProjectTitleAndCreatorUsernameWithOffsetLimit(String query, int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        List<SimpleProjectDto> resultList = em.createQuery(
                        "select new hello.tumblbug.dto.SimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship) " +
                                "from Project p join p.creator m " +
                                "where p.title like :query or m.username like :query " +
                                "order by p.dateCreated desc", SimpleProjectDto.class)
                .setParameter("query", "%" + query + "%")
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        simpleProjectDtosWithTotal.setDtos(resultList);
        if (needTotal) {
            Long total = em.createQuery(
                            "select count(p) from Project p " +
                                    "join p.creator m " +
                                    "where p.title like :query or m.username like :query", Long.class)
                    .setParameter("query", "%" + query + "%")
                    .getSingleResult();
            simpleProjectDtosWithTotal.setTotal(total);
        }
        return simpleProjectDtosWithTotal;
    }
/*
    //검색에 사용
    public SimpleProjectDtosWithTotal findSimpleByQueryStringOnProjectTitleAndCreatorUsernameWithOffsetLimit(String query, int offset, int limit, boolean needTotal) {
        SimpleProjectDtosWithTotal simpleProjectDtosWithTotal = new SimpleProjectDtosWithTotal();
        QProject p = QProject.project;
        QMember m = QMember.member;
        List<SimpleProjectDto> resultList = queryFactory
                .select(new QSimpleProjectDto(p.id, p.title, p.category, m.id, m.username, p.mainImage.storeFileName, p.targetSponsorship, p.currentSponsorship))
                .from(p)
                .join(p.creator, m)
                .where(orCondition(projectTitleContains(query), memberUsernameContains(query)))
                .orderBy(p.dateCreated.desc())
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

    //검색에 사용
    private BooleanExpression projectTitleContains(String query) {
        QProject p = QProject.project;
        return hasText(query) ? p.title.contains(query) : null;
    }

    //검색에 사용
    private BooleanExpression memberUsernameContains(String query) {
        QMember m = QMember.member;
        return hasText(query) ? m.username.contains(query) : null;
    }

    //검색에 사용
    private BooleanExpression orCondition(BooleanExpression e1, BooleanExpression e2) {
        if (e1 == null) {
            return e2;
        }
        return e1.or(e2);
    }
*/
}
