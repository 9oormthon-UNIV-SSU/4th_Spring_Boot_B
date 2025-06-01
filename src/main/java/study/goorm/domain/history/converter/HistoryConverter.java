package study.goorm.domain.history.converter;

import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.member.domain.entity.Member;

import java.util.List;
import java.util.Map;

public class HistoryConverter {

    public static HistoryResponseDTO.MonthlyHistoryDTO toMonthlyHistoryDTO(
        Member member,
        List<History> histories,
        Map<Long, String> firstImageUrlMap
    ) {
            return HistoryResponseDTO.MonthlyHistoryDTO.builder()
                    .memberId(member.getId())
                    .nickName(member.getNickname())
                    .histories(toHistoryBriefDTOList(histories, firstImageUrlMap))
                    .build();
        }

    public static List<HistoryResponseDTO.HistoryBriefDTO> toHistoryBriefDTOList(List<History> histories, Map<Long, String> firstImageUrlMap) {
        return histories.stream()
                .map(history -> HistoryResponseDTO.HistoryBriefDTO.builder()
                        .historyId(history.getId())
                        .date(history.getHistoryDate())
                        .imageUrl(firstImageUrlMap.get(history.getId()))
                        .build())
                .toList();
    }
}
