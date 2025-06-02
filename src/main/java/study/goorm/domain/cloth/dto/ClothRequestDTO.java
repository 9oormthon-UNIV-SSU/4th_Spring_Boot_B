package study.goorm.domain.cloth.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.goorm.domain.model.enums.Season;
import study.goorm.domain.model.enums.ThicknessLevel;
import study.goorm.domain.model.exception.annotation.CheckLowerUpperTempBound;

import java.util.List;

public class ClothRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothCreateRequest {

        private Long memberId;

        private Long categoryId;

        private String name;

        private List<Season> seasons;

        @Max(40)
        @Min(-20)
        @CheckLowerUpperTempBound
        private Integer tempUpperBound;

        @Max(40)
        @Min(-20)
        private Integer tempLowerBound;

        private ThicknessLevel thicknessLevel;

        private String clothUrl;

        private String brand;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @CheckLowerUpperTempBound
    public static class ClothUpdateRequest {

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