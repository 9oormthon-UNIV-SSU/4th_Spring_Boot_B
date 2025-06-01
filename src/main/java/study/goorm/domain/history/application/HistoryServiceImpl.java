package study.goorm.domain.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.goorm.domain.history.converter.HistoryConverter;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.repository.HistoryRepository;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.history.exception.HistoryException;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.member.domain.repository.MemberRepository;
import study.goorm.global.error.code.status.ErrorStatus;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final HistoryImageQueryService historyImageQueryService;

    @Override
    public HistoryResponseDTO.MonthlyHistoryDTO getMonthlyHistories(String clokeyId, YearMonth month) {

        String effectiveClokeyId = (clokeyId == null) ? "clo001" : clokeyId;

        Member member = memberRepository.findByClokeyId(effectiveClokeyId)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY_MEMBER));

        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<History> histories = historyRepository.findByMemberIdAndHistoryDateBetween(member.getId(), start, end);
        Map<Long, String> imageUrlMap = historyImageQueryService.getFirstImageUrlMap(histories);

        return HistoryConverter.toMonthlyHistoryDTO(member, histories, imageUrlMap);
    }
}
