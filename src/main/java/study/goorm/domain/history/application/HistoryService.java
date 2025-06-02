package study.goorm.domain.history.application;

import study.goorm.domain.history.dto.HistoryResponseDTO;

public interface HistoryService {
    HistoryResponseDTO.MonthlyHistoryViewResult getMonthlyHistories(Long clokeyId, String month);
}
