package hello.tumblbug.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    private String loginId;

    private String password;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "FOLLOWER_ID"),
                inverseJoinColumns = @JoinColumn(name = "FOLLOWEE_ID"))
    private List<Member> followings = new ArrayList<>();

    @ManyToMany(mappedBy = "followings")
    private List<Member> followers = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Project> createdProjects = new ArrayList<>();

    @OneToMany(mappedBy = "sponsor")
    private List<MemberProject> memberProjects = new ArrayList<>();
}
