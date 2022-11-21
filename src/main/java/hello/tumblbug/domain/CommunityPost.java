package hello.tumblbug.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter
public class CommunityPost {

    @Id @GeneratedValue
    @Column(name = "COMMUNITY_POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    private String content;

    private LocalDateTime createDate;
}
