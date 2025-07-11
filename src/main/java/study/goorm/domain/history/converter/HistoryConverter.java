package study.goorm.domain.history.converter;


import study.goorm.domain.history.domain.entity.Comment;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.model.enums.Visibility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//List<Cloth>에서 List<HistoryCloth>로 바꿈
public class HistoryConverter {


    public static HistoryResponseDTO.HistoryMonthlyViewResult toHistoryMonthlyViewResult(
            List<History> histories, Member member, Map<Long,String> imageUrlMap
            ) {

        List<HistoryResponseDTO.HistoryMonthlyViewResult.HistoryItem> historyItems = histories.stream()
                .map(history -> new HistoryResponseDTO.HistoryMonthlyViewResult.HistoryItem(
                        history.getId(),
                        history.getHistoryDate(),
                        imageUrlMap.getOrDefault(history.getId(), null) // 존재하지 않으면 null
                ))
                .collect(Collectors.toList());

        return HistoryResponseDTO.HistoryMonthlyViewResult.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .histories(historyItems)
                .build();
    }

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


    //코드 참고
    public static History toHistoryEntity(HistoryRequestDTO.HistoryCreateRequest historyRequestDTO,Member member) {
        return History.builder()
                .member(member)
                .historyDate(historyRequestDTO.getDate())
                .likes(0)
                .content(historyRequestDTO.getContent())
                .build();
    }

    public static HistoryResponseDTO.HistoryCreateResult toHistoryCreateResult(
            History history
    )
    {
        return HistoryResponseDTO.HistoryCreateResult.builder()
                .historyId(history.getId())
                .build();
    }



    public static HistoryResponseDTO.HistoryUpdateResult toHistoryUpdateResult(History history){
        return HistoryResponseDTO.HistoryUpdateResult.builder()
                .historyId(history.getId())
                .build();
    }



    //안쓰이나 남겨둠
    public static HistoryRequestDTO.HistoryCreateRequest toHistoryCreateRequest(
            History history,List<Long> clothes, List<String> hashtags
    ) {
        return HistoryRequestDTO.HistoryCreateRequest.builder()
                .content(history.getContent())
                .clothes(clothes)
                .hashtags(hashtags)
                .date(history.getHistoryDate())
                .build();
    }


    //안쓰이나 남겨둠
    public static HistoryRequestDTO.HistoryUpdateRequest toHistoryUpdateRequest(
            History history, List<Long> clothes, List<String> hashtags, Visibility visibility) {
        return HistoryRequestDTO.HistoryUpdateRequest.builder()
                .content(history.getContent())
                .clothes(clothes)
                .hashtags(hashtags)
                .visibility(visibility.toString())
                .build();
    }

    public static HistoryResponseDTO.LikeResult toHistoryLikeResult(
            History history,boolean newState, Long likeCount

    ){
        return HistoryResponseDTO.LikeResult.builder()
                .historyId(history.getId())
                .isLiked(newState)
                .likeCount(likeCount)
                .build();

    }

    public static HistoryResponseDTO.LikedUser toHistoryLikedUser(
        Member member, boolean me, boolean followStatus
    ){
        return HistoryResponseDTO.LikedUser.builder()
                .memberId(member.getId())
                .clokeyId(member.getClokeyId())
                .nickName(member.getNickname())
                .imageUrl(member.getProfileImageUrl())
                .me(me)
                .followStatus(followStatus)
                .build();

    }

    public static HistoryResponseDTO.LikedUsersResult toLikedUsersResult(
            List<HistoryResponseDTO.LikedUser> likedUsers
    ){

        return HistoryResponseDTO.LikedUsersResult.builder()
                .likedUsers(likedUsers)
                .build();

    }


    public static HistoryResponseDTO.writeCommentResult toHistoryWriteCommentResult(
        Comment comment
    ){
        return HistoryResponseDTO.writeCommentResult.builder()
                .commentId(comment.getId())
                .build();
    }














}
