package hello.tumblbug.domain;

import hello.tumblbug.file.UploadFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(length = DBConst.MEMBER_USERNAME_MAX_LENGTH)
    private String username;

    @Column(length = DBConst.MEMBER_LOGIN_ID_MAX_LENGTH)
    private String loginId;

    @Column(length = DBConst.MEMBER_PASSWORD_MAX_LENGTH)
    private String password;

    @Column(length = DBConst.MEMBER_INFO_MAX_LENGTH)
    private String info;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "FOLLOWER_ID"),
                inverseJoinColumns = @JoinColumn(name = "FOLLOWEE_ID"),
                uniqueConstraints = {@UniqueConstraint(columnNames={"FOLLOWER_ID", "FOLLOWEE_ID"})}
    )
    private List<Member> followings = new ArrayList<>();

    @ManyToMany(mappedBy = "followings")
    private List<Member> followers = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Project> createdProjects = new ArrayList<>();

    @OneToMany(mappedBy = "sponsor")
    private List<MemberProject> memberProjects = new ArrayList<>();

    @Embedded
    private UploadFile userImage;


    public Member(String username, String loginId, String password) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.userImage = new UploadFile(MemberConst.DEFAULT_USER_IMAGE_FILENAME, MemberConst.DEFAULT_USER_IMAGE_FILENAME);
    }

    protected Member() {}
}
