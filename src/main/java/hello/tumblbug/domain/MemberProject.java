package hello.tumblbug.domain;

import javax.persistence.*;

@Entity
public class MemberProject {

    @Id @GeneratedValue
    @Column(name = "MEMBER_PROJECT_ID")
    private Long id;

    @ManyToOne
    private Member sponsor;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Reward reward;
}
