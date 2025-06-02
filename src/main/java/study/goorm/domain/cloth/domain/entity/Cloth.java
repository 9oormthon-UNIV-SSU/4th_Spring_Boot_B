package study.goorm.domain.cloth.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.model.Enum.Season;
import study.goorm.domain.model.Enum.ThicknessLevel;
import study.goorm.domain.model.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cloth extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int wearNumber;

    @ElementCollection(targetClass = Season.class)
    @CollectionTable(name = "cloth_seasons", joinColumns = @JoinColumn(name = "cloth_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false)
    private List<Season> season;

    @Min(-20)
    @Max(40)
    @Column(nullable = false)
    private int tempUpperBound;

    @Min(-20)
    @Max(40)
    @Column(nullable = false)
    private int tempLowerBound;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThicknessLevel thicknessLevel;

    private String clothUrl;

    @Column(length = 50)
    private String brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    public void increaseWearCount() {
        this.wearNumber += 1;
    }

    public void decreaseWearCount() {
        if (this.wearNumber > 0) {
            this.wearNumber -= 1;
        }
    }
}
