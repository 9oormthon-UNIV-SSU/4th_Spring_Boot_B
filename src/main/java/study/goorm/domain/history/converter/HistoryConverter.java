package study.goorm.domain.history.converter;

import org.springframework.data.domain.Page;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.history.domain.entity.Comment;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.dto.HistoryRequestDTO;
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

    public static History toHistoryEntity(HistoryRequestDTO.CreateHistoryDTO dto, Member member) {
        return History.builder()
                .member(member)
                .historyDate(dto.getDate())
                .likes(0)
                .content(dto.getContent())
                .build();
    }

    public static HistoryResponseDTO.CreateHistoryResultDTO toCreateHistoryResultDTO(History history) {
        return HistoryResponseDTO.CreateHistoryResultDTO.builder()
                .historyId(history.getId())
                .build();
    }

    public static HistoryResponseDTO.LikeResponseDTO toLikeResponseDTO(History history, boolean liked) {
        return HistoryResponseDTO.LikeResponseDTO.builder()
                .historyId(history.getId())
                .liked(liked)
                .likeCount(history.getLikes())
                .build();
    }

    public static HistoryResponseDTO.LikedUsersResponseDTO toLikedUsersResponseDTO(
            List<Member> likedMembers
    ) {
        List<HistoryResponseDTO.LikedUserDTO> likedUserDTOs = likedMembers.stream()
                .map(user -> HistoryResponseDTO.LikedUserDTO.builder()
                        .memberId(user.getId())
                        .clokeyId(user.getClokeyId())
                        .nickname(user.getNickname())
                        .imageUrl(user.getProfileUrl())
                        .build())
                .toList();

        return HistoryResponseDTO.LikedUsersResponseDTO.builder()
                .likedUsers(likedUserDTOs)
                .build();
    }

    public static Comment toCommentEntity(History history, Member member, String content, Comment parent) {
        return Comment.builder()
                .history(history)
                .member(member)
                .content(content)
                .comment(parent)
                .build();
    }

    public static HistoryResponseDTO.CommentResultDTO toCommentResultDTO(Comment comment) {
        return HistoryResponseDTO.CommentResultDTO.builder()
                .commentId(comment.getId())
                .build();
    }

    public static HistoryResponseDTO.CommentWithRepliesDTO toCommentWithRepliesDTO(Comment parent,List<Comment> replies) {
        Member member = parent.getMember();

        return HistoryResponseDTO.CommentWithRepliesDTO.builder()
                .commentId(parent.getId())
                .nickname(member.getNickname())
                .clokeyId(member.getClokeyId())
                .imageUrl(member.getProfileUrl())
                .content(parent.getContent())
                .replyResults(toReplyDTOs(replies))
                .build();
    }

    public static List<HistoryResponseDTO.CommentWithRepliesDTO.ReplyDTO> toReplyDTOs(List<Comment> replies) {
        return replies.stream()
                .map(reply -> {
                    Member member = reply.getMember();
                    return HistoryResponseDTO.CommentWithRepliesDTO.ReplyDTO.builder()
                            .commentId(reply.getId())
                            .nickname(member.getNickname())
                            .clokeyId(member.getClokeyId())
                            .imageUrl(member.getProfileUrl())
                            .content(reply.getContent())
                            .build();
                }).toList();
    }

    public static HistoryResponseDTO.CommentsPageDTO toCommentsPageDTO(
            Page<Comment> parentPage,
            Map<Long, List<Comment>> replyMap
    ) {
        List<HistoryResponseDTO.CommentWithRepliesDTO> commentDTOs = parentPage.getContent().stream()
                .map(parent -> {
                    List<Comment> replies = replyMap.getOrDefault(parent.getId(), List.of());
                    return toCommentWithRepliesDTO(parent, replies);
                })
                .toList();

        return HistoryResponseDTO.CommentsPageDTO.builder()
                .comments(commentDTOs)
                .totalPage(parentPage.getTotalPages())
                .totalElements(parentPage.getTotalElements())
                .isFirst(parentPage.isFirst())
                .isLast(parentPage.isLast())
                .build();
    }

}
