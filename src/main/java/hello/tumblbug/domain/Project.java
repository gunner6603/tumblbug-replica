package hello.tumblbug.domain;

import hello.tumblbug.file.UploadFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = DBConst.PROJECT_TITLE_MAX_LENGTH, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member creator;

    @Embedded
    @AttributeOverride(name = "storeFileName", column = @Column(name = "main_image_file_name", nullable = false))
    private UploadFile mainImage;

    @ElementCollection
    @CollectionTable(name = "project_sub_image", joinColumns = @JoinColumn(name = "PROJECT_ID"))
    @AttributeOverride(name = "storeFileName", column = @Column(name = "sub_image_file_name", nullable = false))
    private List<UploadFile> subImages = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    List<MemberProject> memberProjects = new ArrayList<>();

    @NumberFormat(pattern = "###,###")
    @Column(nullable = false)
    private int sponsorCount;

    @NumberFormat(pattern = "###,###")
    @Column(nullable = false)
    private int targetSponsorship;

    @NumberFormat(pattern = "###,###")
    @Column(nullable = false)
    private int currentSponsorship;

    @Column(length = DBConst.PROJECT_DESCRIPTION_MAX_LENGTH, nullable = false)
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reward> rewards = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDateTime deadline;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @OneToMany(mappedBy = "project")
    private List<CommunityPost> communityPosts = new ArrayList<>();

    public void addReward(Reward reward) {
        rewards.add(reward);
        reward.setProject(this);
    }

    public void increaseCurrentSponsorship(int price) {
        currentSponsorship += price;
    }

    public void increaseSponsorCount() {
        sponsorCount++;
    }

    public int getAchievementRate() {
        if (targetSponsorship == 0) {
            return 0;
        }
        return currentSponsorship * 100 / targetSponsorship;
    }

    @Builder
    public Project(String title, Category category, Member creator, UploadFile mainImage, List<UploadFile> subImages, int targetSponsorship, String description, LocalDateTime deadline) {
        this.title = title;
        this.category = category;
        this.creator = creator;
        this.mainImage = mainImage;
        this.subImages = subImages;
        this.targetSponsorship = targetSponsorship;
        this.description = description;
        this.deadline = deadline;
        this.dateCreated = LocalDateTime.now();
    }

    //삭제할 것
    public Project(String title, Category category, Member creator, UploadFile mainImage, int targetSponsorship, String description, LocalDateTime deadline, Reward reward1, Reward reward2) {
        this.title = title;
        this.category = category;
        this.creator = creator;
        this.mainImage = mainImage;
        this.targetSponsorship = targetSponsorship;
        this.description = description;
        this.deadline = deadline;
        this.dateCreated = LocalDateTime.now();
        addReward(reward1);
        addReward(reward2);
    }
}
