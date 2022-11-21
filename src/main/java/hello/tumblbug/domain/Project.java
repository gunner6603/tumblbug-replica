package hello.tumblbug.domain;

import hello.tumblbug.dto.ProjectUploadDto;
import hello.tumblbug.file.UploadFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class Project {

    @Id @GeneratedValue
    @Column(name = "PROJECT_ID")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creator;

    @Embedded
    private UploadFile mainImage;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "PROJECT_ID"))
    private List<UploadFile> subImages = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    List<MemberProject> memberProjects = new ArrayList<>();

    private int targetSponsorship;

    private int currentSponsorship;

    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reward> rewards = new ArrayList<>();

    private LocalDateTime deadline;

    private LocalDateTime createdTime;

    private LocalDateTime lastModifiedTime;

    @OneToMany(mappedBy = "project")
    private List<CommunityPost> communityPosts = new ArrayList<>();

    public void addReward(Reward reward) {
        rewards.add(reward);
        reward.setProject(this);
    }
}
