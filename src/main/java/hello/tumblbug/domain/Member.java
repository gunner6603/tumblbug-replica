package hello.tumblbug.domain;

import hello.tumblbug.domain.encryption.PasswordEncrypt;
import hello.tumblbug.file.UploadFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = DBConst.MEMBER_USERNAME_MAX_LENGTH, nullable = false)
    private String username;

    @Column(length = DBConst.MEMBER_LOGIN_ID_MAX_LENGTH, nullable = false, unique = true)
    private String loginId;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(length = 24, nullable = false)
    private String salt;

    @Column(length = DBConst.MEMBER_INFO_MAX_LENGTH)
    private String info;

    @ManyToMany
    @JoinTable(name = "follow", joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id", "followee_id"})}
    )
    private List<Member> followings = new ArrayList<>();

    @ManyToMany(mappedBy = "followings")
    private List<Member> followers = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Project> createdProjects = new ArrayList<>();

    @OneToMany(mappedBy = "sponsor")
    private List<MemberProject> memberProjects = new ArrayList<>();

    @Embedded
    @AttributeOverride(name = "storeFileName", column = @Column(name = "user_image_file_name", nullable = false))
    private UploadFile userImage;


    public Member(String username, String loginId, String rawPassword) {
        this.username = username;
        this.loginId = loginId;
        setEncryptedPassword(rawPassword);
        this.userImage = new UploadFile(MemberConst.DEFAULT_USER_IMAGE_FILENAME);
    }

    public void update(UploadFile userImage, String username, String password, String info) {
        if (userImage != null) {
            this.userImage = userImage;
        }
        this.username = username;
        setEncryptedPassword(password);
        this.info = info;
    }

    private void setEncryptedPassword(String rawPassword) {
        try {
            String salt = PasswordEncrypt.getSalt();
            String encryptedPassword = PasswordEncrypt.encrypt(rawPassword, salt);
            this.salt = salt;
            this.password = encryptedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //삭제할 것
    public void setInfo(String info) {
        this.info = info;
    }
}
