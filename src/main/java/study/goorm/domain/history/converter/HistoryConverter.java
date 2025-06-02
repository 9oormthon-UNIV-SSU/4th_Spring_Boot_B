package study.goorm.domain.history.converter;

import study.goorm.domain.cloth.domain.entity.Cloth;
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

    public static HistoryResponseDTO.DailyHistoryDTO toDailyHistoryDTO(
            History history,
            Map<Long, String> firstImageUrlMap,
            List<String> hashtags,
            List<Cloth> clothes,
            Map<Long, String> firstImagesOfCloth,
            boolean liked,
            int commentCount
    ) {
        Member member = history.getMember();

        return HistoryResponseDTO.DailyHistoryDTO.builder()
                .historyId(history.getId())
                .memberId(member.getId())
                .clokeyId(member.getClokeyId())
                .nickName(member.getNickname())
                .memberImageUrl(member.getProfileUrl())
                .contents(history.getContent())
                .imageUrl(List.of(firstImageUrlMap.get(history.getId())))
                .hashtags(hashtags)
                .likeCount(history.getLikes())
                .commentCount(commentCount)
                .liked(liked)
                .date(history.getHistoryDate())
                .cloths(toClothPreviewDTOList(clothes,firstImagesOfCloth))
                .build();
    }

    public static List<HistoryResponseDTO.DailyHistoryDTO.ClothPreviewDTO> toClothPreviewDTOList(List<Cloth> clothes,Map<Long, String> firstImagesOfCloth) {
        return clothes.stream()
                .map(cloth -> HistoryResponseDTO.DailyHistoryDTO.ClothPreviewDTO.builder()
                        .clothId(cloth.getId())
                        .clothImageUrl(firstImagesOfCloth.get(cloth.getId()))  // 엔티티에서 대표 이미지 추출
                        .clothName(cloth.getName())
                        .build())
                .toList();
    }
}
