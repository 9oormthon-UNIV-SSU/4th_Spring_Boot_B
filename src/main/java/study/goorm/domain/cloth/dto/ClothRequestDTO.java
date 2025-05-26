package study.goorm.domain.cloth.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import study.goorm.domain.cloth.exception.annotation.CheckLowerUpperTempBound;
import study.goorm.domain.model.Enum.Season;
import study.goorm.domain.model.Enum.ThicknessLevel;

import java.util.List;


public class ClothRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @CheckLowerUpperTempBound
    public static class ClothCreateRequest {
        private Long memberId;

        private Long categoryId;

        private String name;

        private List<Season> seasons;

        @Max(40)
        @Min(-20)
        private Integer tempUpperBound;

        @Max(40)
        @Min(-20)
        private Integer tempLowerBound;

        private ThicknessLevel thicknessLevel;

        private String clothUrl;

        private String brand;

    }

}
