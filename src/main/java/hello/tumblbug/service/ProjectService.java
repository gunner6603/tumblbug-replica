package hello.tumblbug.service;

import hello.tumblbug.domain.*;
import hello.tumblbug.dto.*;
import hello.tumblbug.repository.MemberProjectRepository;
import hello.tumblbug.repository.MemberRepository;
import hello.tumblbug.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;

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
        project.setCreatedTime(LocalDateTime.now());
        project.setLastModifiedTime(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public Project findOne(Long id) {
        return projectRepository.findById(id);
    }

    public List<SimpleProjectDto> findAllAsSimpleProjectDto() {
        return projectRepository.findAllAsSimpleProjectDto();
    }

    public List<SimpleProjectDto> findLatestN(int n) {
        return projectRepository.findAllSimpleByTimeDescWithOffsetLimit(0, n, false).getDtos();
    }

    public List<SimpleProjectDto> findMostPopularN(int n) {
        return projectRepository.findAllSimpleByCurrentSponsorshipDescWithOffsetLimit(0, n, false).getDtos();
    }

    public PagingDto<SimpleProjectDto> findLatestByPagingDto(PagingQueryDto queryDto) {
        SimpleProjectDtoWithTotal dtoWithTotal = projectRepository.findAllSimpleByTimeDescWithOffsetLimit(queryDto.getOffset(), queryDto.getLimit(), true);
        PagingDto<SimpleProjectDto> pagingDto = new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
        return pagingDto;
    }

    public PagingDto<SimpleProjectDto> findMostPopularByPagingDto(PagingQueryDto queryDto) {
        SimpleProjectDtoWithTotal dtoWithTotal = projectRepository.findAllSimpleByCurrentSponsorshipDescWithOffsetLimit(queryDto.getOffset(), queryDto.getLimit(), true);
        PagingDto<SimpleProjectDto> pagingDto = new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
        return pagingDto;
    }

    public PagingDto<SimpleProjectDto> findMostImminentByPagingDto(PagingQueryDto queryDto) {
        SimpleProjectDtoWithTotal dtoWithTotal = projectRepository.findAllNotExpiredSimpleByDeadlineAscWithOffsetLimit(queryDto.getOffset(), queryDto.getLimit(), true);
        PagingDto<SimpleProjectDto> pagingDto = new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
        return pagingDto;
    }

    public PagingDto<SimpleProjectDto> findByCategory(Category category, PagingQueryDto queryDto) {
        SimpleProjectDtoWithTotal dtoWithTotal = projectRepository.findAllSimpleByCategoryAndTimeDescWithOffsetLimit(category, queryDto.getOffset(), queryDto.getLimit(), true);
        PagingDto<SimpleProjectDto> pagingDto = new PagingDto<>(queryDto.getPageNum(), queryDto.getLimit(), dtoWithTotal.getTotal(), dtoWithTotal.getDtos());
        return pagingDto;
    }

    public List<SimpleProjectDto> findCreatedProject(Long memberId) {
        return projectRepository.findAllSimpleByCreatorId(memberId);
    }

    public List<SimpleProjectDto> findSponsoredProject(Long memberId) {
        return projectRepository.findAllSimpleBySponsorId(memberId);
    }

    public void sponsorProject(Long memberId, Long projectId, Integer rewardNum) {
        Member member = memberRepository.findById(memberId);
        Project project = projectRepository.findById(projectId);
        Reward reward = project.getRewards().get(rewardNum - 1);
        MemberProject memberProject = new MemberProject(member, project, reward);
        memberProjectRepository.save(memberProject);
        project.increaseCurrentSponsorship(reward.getPrice());
    }
}
