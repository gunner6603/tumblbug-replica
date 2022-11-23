package hello.tumblbug.dto;

import hello.tumblbug.domain.*;
import lombok.Data;


@Data
public class SimpleProjectDto { //목록 출력용 DTO : 프로젝트에 대한 간략 정보 포함

    private Long projectId;

    private String title;

    private Category category;

    private Long creatorId;

    private String creatorUsername;

    private String mainImageStoreFileName;

    private int targetSponsorship;

    private int currentSponsorship;

    private int achievementRate;

    public SimpleProjectDto(Long projectId, String title, Category category, Long creatorId, String creatorUsername, String mainImageStoreFileName, int targetSponsorship, int currentSponsorship) {
        this.projectId = projectId;
        this.title = title;
        this.category = category;
        this.creatorId = creatorId;
        this.creatorUsername = creatorUsername;
        this.mainImageStoreFileName = mainImageStoreFileName;
        this.targetSponsorship = targetSponsorship;
        this.currentSponsorship = currentSponsorship;
        this.achievementRate = currentSponsorship * 100 / targetSponsorship;
    }
}
