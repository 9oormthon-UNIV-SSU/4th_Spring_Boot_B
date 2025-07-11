package study.goorm.domain.history.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.goorm.domain.cloth.domain.entity.ClothImage;




import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryMonthlyViewResult{
        private Long memberId;
        private String nickname;
        private List<HistoryItem> histories;

        @Getter
        @AllArgsConstructor
        public static class HistoryItem{
            private Long historyId;
            private LocalDate date;
            private String imageUrl;
        }
    }


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
    public static class HistoryCreateResult{
        private Long historyId;
    }




    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryUpdateResult{
        private Long historyId;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeResult{
        private Long historyId;
        private boolean isLiked;
        private long likeCount;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedUser{
        private Long memberId;
        private String clokeyId;
        private String nickName;
        private boolean followStatus;
        private String imageUrl;
        private Boolean me;

    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedUsersResult{
        private List<LikedUser> likedUsers;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class writeCommentResult{
        private Long commentId;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HistoryCommentProjectionDTO {
        private Long commentId;
        private String content;
        private boolean isRoot;
        private Long parentId;
        private String clokeyId;
        private String nickname;
        private String profileImageUrl;
        private LocalDateTime createdAt;
    }










}
