package study.goorm.domain.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class HistoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyHistoryViewResult {
        private Long memberId;
        private String nickName;
        private List<HistoryItem> histories;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class HistoryItem {
        private Long historyId;
        private LocalDate date;
        private String imageUrl;
    }
}
