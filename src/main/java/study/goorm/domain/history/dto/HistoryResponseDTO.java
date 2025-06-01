package study.goorm.domain.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HistoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryUpdateResult {
        // 더미 필드 추가로 Jackson 직렬화 문제 해결
        private String message = "기록이 성공적으로 수정되었습니다.";
    }
}