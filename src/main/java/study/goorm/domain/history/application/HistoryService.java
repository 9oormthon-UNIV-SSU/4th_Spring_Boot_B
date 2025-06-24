package study.goorm.domain.history.application;

import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.history.domain.entity.Hashtag;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.model.enums.Visibility;

import java.time.YearMonth;

public interface HistoryService {
    HistoryResponseDTO.HistoryDailyViewResult getDailyHistoryView(Long historyId);
    HistoryResponseDTO.HistoryMonthlyViewResult getMonthlyHistoryView(String clokeyId, YearMonth month);
    HistoryResponseDTO.HistoryUpdateResult updateHistory(HistoryRequestDTO.HistoryUpdateRequest historyUpdateResult);
    public void deleteHistory(Long historyId);

    HistoryResponseDTO.HistoryCreateResult createHistory(HistoryRequestDTO.HistoryCreateRequest historyCreateRequest);

}

