package study.goorm.domain.history.application;

import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;

import java.util.List;

public interface HistoryService {

    HistoryResponseDTO.HistoryUpdateResult updateHistory(Long historyId, HistoryRequestDTO.HistoryUpdateRequest historyUpdateRequest, List<MultipartFile> images, Long memberId);

    void deleteHistory(Long historyId, Long memberId);
}