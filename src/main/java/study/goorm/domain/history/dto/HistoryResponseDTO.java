package study.goorm.domain.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.goorm.domain.cloth.domain.entity.ClothImage;
import study.goorm.domain.model.enums.Visibility;


import java.time.LocalDate;
import java.util.List;

public class HistoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryDailyViewResult{
        private Long memberId;
        private String memberImageUrl;
        private String nickName;
        private String clokeyId;
        private String contents;
        private List<ClothImage> images;
        private List<String> hashtags;
        private int likeCount;
        private Boolean liked;
        private LocalDate date;
        private List<String> clothes;
        private Long ClothId;
        private int commentCount;
        private Long historyId;


    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryUpdateResult{
        private String content;
        private List<Long> clothes;
        private List<String> hashtags;
        private Visibility visibility;
    }


}
