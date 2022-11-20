package hello.tumblbug.domain;

import hello.tumblbug.file.UploadFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id @GeneratedValue
    @Column(name = "PROJECT_ID")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    private Member creator;

    @Embedded
    private UploadFile mainImage;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "PROJECT_ID"))
    private List<UploadFile> subImages = new ArrayList<>();

    private int targetSponsorship;

    private int currentSponsorship;

    private String description;

    @OneToMany(mappedBy = "project")
    private List<Reward> rewards = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deadline;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "project")
    private List<CommunityPost> communityPosts = new ArrayList<>();
}
