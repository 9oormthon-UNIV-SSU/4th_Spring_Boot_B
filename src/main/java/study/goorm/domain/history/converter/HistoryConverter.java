package study.goorm.domain.history.converter;

import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.entity.Hashtag;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.model.enums.Visibility;

import java.util.List;
//List<Cloth>에서 List<HistoryCloth>로 바꿈
public class HistoryConverter {
    public static HistoryResponseDTO.HistoryDailyViewResult toHistoryDailyViewResult(
            History history, List<String> hashtag, List<String> clothes, int commentCount, boolean liked
    ) {
        return HistoryResponseDTO.HistoryDailyViewResult.builder()
                .memberId(history.getMember().getId())
                .historyId(history.getId())
                .memberImageUrl(history.getMember().getProfileImageUrl())
                .nickName(history.getMember().getNickname())
                .clokeyId(history.getMember().getClokeyId())
                .contents(history.getContent())
                .hashtags(hashtag)
                .likeCount(history.getLikes())
                .commentCount(commentCount)
                .date(history.getHistoryDate())
                .clothes(clothes)
                .liked(liked)
                .build();

    }

    public static HistoryRequestDTO.HistoryUpdateRequest toHistoryUpdateRequest(History history) {
        return HistoryRequestDTO.HistoryUpdateRequest.builder()
                .historyId(history.getId())
                .build();
    }
    public static HistoryResponseDTO.HistoryUpdateResult toHistoryUpdateResult(
            History history, List<Long> clothes, List<String> hashtags, Visibility visibility) {
        return HistoryResponseDTO.HistoryUpdateResult.builder()
                .content(history.getContent())
                .clothes(clothes)
                .hashtags(hashtags)
                .visibility(visibility)
                .build();
    }

}
