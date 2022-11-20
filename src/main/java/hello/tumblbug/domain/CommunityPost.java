package hello.tumblbug.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CommunityPost {

    @Id @GeneratedValue
    @Column(name = "COMMUNITY_POST_ID")
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Member author;

    private String content;

    @Temporal(TemporalType.DATE)
    private LocalDateTime createDate;
}
