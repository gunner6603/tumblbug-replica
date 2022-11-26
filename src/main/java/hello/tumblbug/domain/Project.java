package hello.tumblbug.domain;

import hello.tumblbug.dto.ProjectUploadDto;
import hello.tumblbug.file.UploadFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

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

    @NumberFormat(pattern = "###,###")
    private int targetSponsorship;

    @NumberFormat(pattern = "###,###")
    private int currentSponsorship;

    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reward> rewards = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime deadline;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdTime;

    private LocalDateTime lastModifiedTime;

    @OneToMany(mappedBy = "project")
    private List<CommunityPost> communityPosts = new ArrayList<>();

    public void addReward(Reward reward) {
        rewards.add(reward);
        reward.setProject(this);
    }

    public void increaseCurrentSponsorship(int price) {
        currentSponsorship += price;
    }

    public Project() {
    }

    public Project(String title, Category category, Member creator, UploadFile mainImage, int targetSponsorship, String description, LocalDateTime deadline, Reward reward1, Reward reward2) {
        this.title = title;
        this.category = category;
        this.creator = creator;
        this.mainImage = mainImage;
        this.targetSponsorship = targetSponsorship;
        this.description = description;
        this.deadline = deadline;
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
        addReward(reward1);
        addReward(reward2);
    }
}
