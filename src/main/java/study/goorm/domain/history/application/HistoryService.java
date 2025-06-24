package study.goorm.domain.history.application;

import study.goorm.domain.history.dto.HistoryResponseDTO;

import java.time.LocalDate;
import java.time.YearMonth;

public interface HistoryService {
    HistoryResponseDTO.MonthlyHistoryViewResult getMonthlyHistories(String clokeyId, YearMonth historyDate);
}
