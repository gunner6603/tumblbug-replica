package hello.tumblbug.service;

import hello.tumblbug.domain.*;
import hello.tumblbug.dto.*;
import hello.tumblbug.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final CommunityPostRepository communityPostRepository;
    private final RewardRepository rewardRepository;

    public Long createProject(ProjectUploadDto dto) {
        Project project = new Project();
        project.setTitle(dto.getTitle());
        project.setCategory(dto.getCategory());
        project.setCreator(dto.getCreator());
        project.setMainImage(dto.getMainImage());
        project.setSubImages(dto.getSubImages());
        project.setDescription(dto.getDescription());
        project.setTargetSponsorship(dto.getTargetSponsorship());
        project.addReward(dto.getReward1());
        project.addReward(dto.getReward2());
        project.setDeadline(dto.getDeadline());
        project.setDateCreated(LocalDateTime.now());
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Project findOne(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Project findOneFetchCreatorAndReward(Long id) {
        return projectRepository.findByIdFetchCreatorAndReward(id);
    }

    @Transactional(readOnly = true)
    public List<SimpleProjectDto> findLatestN(int n) {
        return projectRepository.findSimpleByTimeDescWithOffsetLimit(0, n, false).getDtos();
    }

    @Transactional(readOnly = true)
    public List<SimpleProjectDto> findMostPopularN(int n) {
        return projectRepository.findSimpleByCurrentSponsorshipDescWithOffsetLimit(0, n, false).getDtos();
    }

    @Transactional(readOnly = true)
    public PagingDto<SimpleProjectDto> findLatestByPagingDto(PagingQueryDto queryDto) {
        SimpleProjectDtosWithTotal dtoWithTotal = projectRepository.findSimpleByTimeDescWithOffsetLimit(queryDto.getOffset(), queryDto.getLimit(), true);
        PagingDto<SimpleProjectDto> pagingDto = new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
        return pagingDto;
    }

    @Transactional(readOnly = true)
    public PagingDto<SimpleProjectDto> findMostPopularByPagingDto(PagingQueryDto queryDto) {
        SimpleProjectDtosWithTotal dtoWithTotal = projectRepository.findSimpleByCurrentSponsorshipDescWithOffsetLimit(queryDto.getOffset(), queryDto.getLimit(), true);
        PagingDto<SimpleProjectDto> pagingDto = new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
        return pagingDto;
    }

    @Transactional(readOnly = true)
    public PagingDto<SimpleProjectDto> findMostImminentByPagingDto(PagingQueryDto queryDto) {
        SimpleProjectDtosWithTotal dtoWithTotal = projectRepository.findNotExpiredSimpleByDeadlineAscWithOffsetLimit(queryDto.getOffset(), queryDto.getLimit(), true);
        PagingDto<SimpleProjectDto> pagingDto = new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
        return pagingDto;
    }

    @Transactional(readOnly = true)
    public PagingDto<SimpleProjectDto> findByCategory(Category category, PagingQueryDto queryDto) {
        SimpleProjectDtosWithTotal dtoWithTotal = projectRepository.findSimpleByCategoryAndTimeDescWithOffsetLimit(category, queryDto.getOffset(), queryDto.getLimit(), true);
        PagingDto<SimpleProjectDto> pagingDto = new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
        return pagingDto;
    }

    @Transactional(readOnly = true)
    public List<SimpleProjectDto> findCreatedProject(Long memberId) {
        return projectRepository.findAllSimpleByCreatorId(memberId);
    }

    @Transactional(readOnly = true)
    public List<SimpleProjectDto> findSponsoredProject(Long memberId) {
        return projectRepository.findAllSimpleBySponsorId(memberId)
                .stream().distinct().collect(Collectors.toList()); //중복 제거
    }

    public void sponsorProject(Long memberId, Long projectId, Long rewardId) {
        Member member = memberRepository.findById(memberId);
        Project project = projectRepository.findById(projectId);
        Reward reward = rewardRepository.findById(rewardId);
        MemberProject memberProject = new MemberProject(member, project, reward);
        memberProjectRepository.save(memberProject);
        project.increaseCurrentSponsorship(reward.getPrice());
        project.increaseSponsorCount();
        reward.increaseSalesCount();
    }

    @Transactional(readOnly = true)
    public PagingDto<SimpleProjectDto> search(PagingQueryDto queryDto, String query) {
        SimpleProjectDtosWithTotal dtoWithTotal = projectRepository.findSimpleByQueryStringOnProjectTitleAndCreatorUsernameWithOffsetLimit(query, queryDto.getOffset(), queryDto.getLimit(), true);
        return new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
    }

    @Transactional(readOnly = true)
    public List<CommunityPost> findCommunityPosts(Long projectId) {
        return communityPostRepository.findByProjectIdFetchAuthor(projectId);
    }

    public void addCommunityPost(Long projectId, Long authorId, String content) {
        Project project = projectRepository.findById(projectId);
        Member author = memberRepository.findById(authorId);
        CommunityPost communityPost = new CommunityPost(project, author, content);
        communityPostRepository.save(communityPost);
    }

    public void deleteCommunityPost(Long postId) {
        communityPostRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public CommunityPost findCommunityPost(Long postId) {
        return communityPostRepository.findById(postId);
    }
}
