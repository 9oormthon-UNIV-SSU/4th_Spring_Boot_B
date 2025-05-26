package study.goorm.domain.cloth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.goorm.domain.model.Enum.Season;
import study.goorm.domain.model.Enum.ThicknessLevel;

import java.util.List;

public class ClothResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothEditViewDTO {
        private Long id;
        private String name;
        private List<Season> seasons;
        private int tempUpperBound;
        private int tempLowerBound;
        private ThicknessLevel thicknessLevel;
        private String clothUrl;
        private String brand;
        private String imageUrl;
        private Long categoryId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberClosetDTO {
        private String nickName;
        private ClothPreviewListDTO clothPreviewListDTO;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothPreviewListDTO {
        private List<ClothPreviewDTO> clothPreviews;
        private int totalPage;
        private long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothPreviewDTO {
        private Long id;
        private String name;
        private String imageUrl;
        private int wearNum;
    }

}
