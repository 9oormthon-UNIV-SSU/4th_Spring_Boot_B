package study.goorm.domain.history.application;

import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;

import java.time.YearMonth;
import java.util.List;

public interface HistoryService {
    HistoryResponseDTO.MonthlyHistoryDTO getMonthlyHistories(String clokeyId, YearMonth month);
    HistoryResponseDTO.DailyHistoryDTO getDailyHistory(Long historyId);
    HistoryResponseDTO.CreateHistoryResultDTO createHistory(HistoryRequestDTO.CreateHistoryDTO requestDTO, List<MultipartFile> imageFiles);
}

