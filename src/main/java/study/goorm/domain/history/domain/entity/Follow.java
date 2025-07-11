package study.goorm.domain.history.domain.entity;

import jakarta.persistence.*;
import study.goorm.domain.member.domain.entity.Member;

@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Member follower;

    @ManyToOne
    private Member following;
}
