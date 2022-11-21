package hello.tumblbug.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Reward {

    @Id @GeneratedValue
    @Column(name = "REWARD_ID")
    private Long id;

    private int price;

    private String description;

    private int salesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public Reward(int price, String description) {
        this.price = price;
        this.description = description;
    }
}
