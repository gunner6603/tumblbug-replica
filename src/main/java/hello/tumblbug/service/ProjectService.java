package hello.tumblbug.service;

import hello.tumblbug.domain.Project;
import hello.tumblbug.dto.ProjectUploadDto;
import hello.tumblbug.dto.SimpleProjectDto;
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
        return projectRepository.findAllSimpleByTimeDescWithOffsetLimit(0, n);
    }

    public List<SimpleProjectDto> findMostPopularN(int n) {
        return projectRepository.findAllSimpleByCurrentSponsorshipDescWithOffsetLimit(0, n);
    }

    public List<SimpleProjectDto> findCreatedProject(Long memberId) {
        return projectRepository.findAllSimpleByCreatorId(memberId);
    }
}
