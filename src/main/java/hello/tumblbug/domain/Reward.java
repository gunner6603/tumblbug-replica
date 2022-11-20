package hello.tumblbug.domain;

import javax.persistence.*;

@Entity
public class Reward {

    @Id @GeneratedValue
    @Column(name = "REWARD_ID")
    private Long id;

    private int price;

    private String description;

    private int salesCount;

    @ManyToOne
    private Project project;
}
