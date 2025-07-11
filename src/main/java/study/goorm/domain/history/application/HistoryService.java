package study.goorm.domain.history.application;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;


import java.time.YearMonth;
import java.util.List;

public interface HistoryService {
    HistoryResponseDTO.HistoryDailyViewResult getDailyHistoryView(Long historyId);
    HistoryResponseDTO.HistoryMonthlyViewResult getMonthlyHistoryView(String clokeyId, YearMonth month);
    HistoryResponseDTO.HistoryUpdateResult updateHistory(Long historyId,HistoryRequestDTO.HistoryUpdateRequest historyUpdateRequest,List<MultipartFile> imageFiles);
    public void deleteHistory(Long historyId);

    HistoryResponseDTO.HistoryCreateResult createHistory(HistoryRequestDTO.HistoryCreateRequest historyCreateRequest, List<MultipartFile> imageFiles);

    HistoryResponseDTO.LikeResult likeHistory(HistoryRequestDTO.LikeRequest likeRequest);

    HistoryResponseDTO.LikedUsersResult likedUser(Long historyId);

    HistoryResponseDTO.writeCommentResult writeComment(Long historyId,HistoryRequestDTO.WriteCommentRequest request);





}

