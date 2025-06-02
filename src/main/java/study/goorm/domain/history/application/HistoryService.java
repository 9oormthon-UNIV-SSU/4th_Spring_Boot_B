package study.goorm.domain.history.application;

import study.goorm.domain.history.dto.HistoryResponseDTO;

import java.time.YearMonth;

public interface HistoryService {
    HistoryResponseDTO.MonthlyHistoryDTO getMonthlyHistories(String clokeyId, YearMonth month);
    HistoryResponseDTO.DailyHistoryDTO getDailyHistory(Long historyId);
}

