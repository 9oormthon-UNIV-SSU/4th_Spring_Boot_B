package study.goorm.domain.history.converter;

import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.member.domain.entity.Member;

import java.util.List;

public class HistoryConverter {

    public static HistoryResponseDTO.MonthlyHistoryViewResult toMonthlyHistoryViewResult(Member member, List<HistoryResponseDTO.HistoryItem> historyItems) {
        return HistoryResponseDTO.MonthlyHistoryViewResult.builder()
                .memberId(member.getId())
                .nickName(member.getNickname())
                .histories(historyItems)
                .build();
    }

    public static HistoryResponseDTO.HistoryItem toHistoryItem(History history, String imageUrl) {
        return HistoryResponseDTO.HistoryItem.builder()
                .historyId(history.getId())
                .date(history.getHistoryDate())
                .imageUrl(imageUrl)
                .build();
    }
}
