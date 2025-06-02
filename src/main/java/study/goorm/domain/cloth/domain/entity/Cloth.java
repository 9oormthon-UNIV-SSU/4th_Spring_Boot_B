package study.goorm.domain.cloth.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import study.goorm.domain.cloth.dto.ClothRequestDTO;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.model.entity.BaseEntity;
import study.goorm.domain.model.enums.Season;
import study.goorm.domain.model.enums.ThicknessLevel;

import java.util.List;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cloth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int wearNum;

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

    private String brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updatePartially(ClothRequestDTO.ClothUpdateRequest request, Category category) {
        // 옷 이름 업데이트
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            this.name = request.getName().trim();
        }

        // 브랜드 업데이트
        if (request.getBrand() != null && !request.getBrand().trim().isEmpty()) {
            this.brand = request.getBrand().trim();
        }

        // 상한 온도 업데이트
        if (request.getTempUpperBound() != null) {
            this.tempUpperBound = request.getTempUpperBound();
        }

        // 하한 온도 업데이트
        if (request.getTempLowerBound() != null) {
            this.tempLowerBound = request.getTempLowerBound();
        }

        // 두께 레벨 업데이트
        if (request.getThicknessLevel() != null) {
            this.thicknessLevel = request.getThicknessLevel();
        }

        // 옷 URL 업데이트
        if (request.getClothUrl() != null && !request.getClothUrl().trim().isEmpty()) {
            this.clothUrl = request.getClothUrl().trim();
        }

        // 카테고리 업데이트
        if (category != null) {
            this.category = category;
        }

        // 계절 정보 업데이트
        if (request.getSeasons() != null && !request.getSeasons().isEmpty()) {
            this.season = request.getSeasons();
        }
    }

    public void increaseWearNum() {
        this.wearNum++;
    }

    public void decreaseWearNum() {
        if (this.wearNum > 0) {
            this.wearNum--;
        }
    }
}