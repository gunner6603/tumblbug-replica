package hello.tumblbug.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor
public class CommunityPost {

    @Id @GeneratedValue
    @Column(name = "COMMUNITY_POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    @Column(length = DBConst.COMMUNITY_POST_CONTENT_MAX_LENGTH)
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createDate;

    public CommunityPost(Project project, Member author, String content) {
        this.project = project;
        this.author = author;
        this.content = content;
        this.createDate = LocalDateTime.now();
    }
}
