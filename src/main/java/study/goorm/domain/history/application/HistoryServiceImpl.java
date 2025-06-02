package study.goorm.domain.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.repository.HistoryRepository;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.history.exception.HistoryException;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;


    @Override
    public HistoryResponseDTO.MonthlyHistoryViewResult getMonthlyHistories(Long clokeyId, String month) {
        // 1. 날짜 포맷 검증
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.parse(month);
        } catch (DateTimeParseException e) {
            throw new HistoryException(HistoryErrorCode.INVALID_MONTH_FORMAT);
        }

        // 2. clokeyId가 null이면 본인 ID 사용
        Long targetClokeyId = (clokeyId != null) ? clokeyId : clokeyService.getCurrentClokeyId();

        // 3. 유효한 사용자 여부 확인
        ClokeyMember member = clokeyService.findById(targetClokeyId)
                .orElseThrow(() -> new HistoryException(HistoryErrorCode.CLOKEY_NOT_FOUND));

        // 4. 기록 조회
        List<History> histories = historyRepository.findByClokeyIdAndMonth(targetClokeyId, yearMonth);

        // 5. 응답 변환
        List<HistoryResponseDTO.HistoryInfo> historyDTOs = histories.stream()
                .map(history -> {
                    String imageUrl = history.getPhotos().isEmpty()
                            ? null
                            : Optional.ofNullable(history.getPhotos().get(0).getImageUrl()).orElse("비공개입니다");
                    return new HistoryResponseDTO.HistoryInfo(
                            history.getId(),
                            history.getDate().toString(),
                            imageUrl
                    );
                })
                .collect(Collectors.toList());

        // 6. 최종 결과 생성
        return new HistoryResponseDTO.MonthlyHistoryViewResult(
                member.getId(),
                member.getNickName(),
                historyDTOs
        );
}
