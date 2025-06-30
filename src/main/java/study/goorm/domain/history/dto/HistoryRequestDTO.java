package study.goorm.domain.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class HistoryRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateHistoryDTO {
        private String content;
        private List<Long> clothes;
        private List<String> hashtags;
        private LocalDate date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateHistoryDTO {
        private String content;
        private List<Long> clothes;
        private List<String> hashtags;
        private String visibility; // ENUM 형태일 경우 타입 맞춰주세요
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeRequestDTO {
        private Long historyId;
        private Boolean liked;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommentRequestDTO {
        private Long commentId;
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateCommentDTO {
        private String content;
    }
}
