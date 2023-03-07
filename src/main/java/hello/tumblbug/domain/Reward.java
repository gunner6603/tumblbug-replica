package hello.tumblbug.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward {

    @Id @GeneratedValue
    private Long id;

    @NumberFormat(pattern = "###,###")
    @Column(nullable = false)
    private int price;

    @Column(length = DBConst.REWARD_DESCRIPTION_MAX_LENGTH, nullable = false)
    private String description;

    @Column(nullable = false)
    private int salesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Reward(int price, String description) {
        this.price = price;
        this.description = description;
    }

    public void increaseSalesCount() {
        salesCount++;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
