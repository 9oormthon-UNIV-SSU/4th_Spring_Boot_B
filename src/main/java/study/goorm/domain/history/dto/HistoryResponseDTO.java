package study.goorm.domain.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class HistoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyHistoryDTO {
        private Long memberId;
        private String nickName;
        private List<HistoryBriefDTO> histories;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryBriefDTO {
        private Long historyId;
        private LocalDate date;
        private String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyHistoryDTO {
        private Long historyId;
        private Long memberId;
        private String clokeyId;
        private String nickName;
        private String memberImageUrl;
        private String contents;
        private List<String> imageUrl;
        private List<String> hashtags;
        private int likeCount;
        private int commentCount;
        private boolean liked;
        private LocalDate date;
        private List<ClothPreviewDTO> cloths;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ClothPreviewDTO {
            private Long clothId;
            private String clothImageUrl;
            private String clothName;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateHistoryResultDTO {
        private Long historyId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeResponseDTO {
        private Long historyId;
        private Boolean liked;
        private int likeCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedUsersResponseDTO {
        private List<LikedUserDTO> likedUsers;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedUserDTO {
        private Long memberId;
        private String clokeyId;
        private String nickname;
        private String imageUrl;
    }

}
