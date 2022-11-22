package hello.tumblbug.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Reward {

    @Id @GeneratedValue
    @Column(name = "REWARD_ID")
    private Long id;

    @NumberFormat(pattern = "###,###")
    private int price;

    private String description;

    private int salesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public Reward(int price, String description) {
        this.price = price;
        this.description = description;
    }

    public Reward() {
    }
}
