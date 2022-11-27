package hello.tumblbug.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MemberProject {

    @Id @GeneratedValue
    @Column(name = "MEMBER_PROJECT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member sponsor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reward reward;

    private LocalDateTime sponsoredTime;

    public MemberProject(Member sponsor, Project project, Reward reward) {
        this.sponsor = sponsor;
        this.project = project;
        this.reward = reward;
        this.sponsoredTime = LocalDateTime.now();
    }
}
