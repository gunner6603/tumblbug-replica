package hello.tumblbug.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
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
}
