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

    private String username;

    private String loginId;

    private String password;

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

    public String userImageStoreFileName;

    public Member(String username, String loginId, String password) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.userImage = new UploadFile(MemberConst.DEFAULT_USER_IMAGE_FILENAME, MemberConst.DEFAULT_USER_IMAGE_FILENAME);
        this.userImageStoreFileName = MemberConst.DEFAULT_USER_IMAGE_FILENAME;
    }

    protected Member() {}
}
