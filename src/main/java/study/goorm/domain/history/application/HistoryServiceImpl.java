package study.goorm.domain.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.cloth.domain.repository.ClothRepository;
import study.goorm.domain.history.converter.HistoryConverter;
import study.goorm.domain.history.domain.entity.*;
import study.goorm.domain.history.domain.repository.*;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.history.exception.HistoryException;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.member.domain.repository.MemberRepository;
import study.goorm.global.common.utils.MinioUploader;
import study.goorm.global.error.code.status.ErrorStatus;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final CommentRepository commentRepository;
    private final HistoryClothRepository historyClothRepository;
    private final HashtagHistoryRepository historyHashtagRepository;
    private final HashtagHistoryRepository hashtagHistoryRepository;
    private final HistoryImageRepository historyImageRepository;
    private final MemberRepository memberRepository;
    private final HistoryImageQueryService historyImageQueryService;
    private final ClothRepository clothRepository;
    private final HashtagRepository hashtagRepository;
    private final MinioUploader minioUploader;
    private final MemberLikeRepository memberLikeRepository;
    private final followRepository followRepository;


    @Override
    @Transactional(readOnly = true)
    public HistoryResponseDTO.HistoryMonthlyViewResult getMonthlyHistoryView(String clokeyId, YearMonth month){

        String effectiveClokeyId=(clokeyId==null) ? "clo001" : clokeyId;

        Member member=memberRepository.findByClokeyId(effectiveClokeyId)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_HISTORY_MEMBER));

        LocalDate start=month.atDay(1);
        LocalDate end=month.atEndOfMonth();
        List<History> histories=historyRepository.findByMemberIdAndHistoryDateBetween(member.getId(),start,end);
        Map<Long,String> imageUrlMap=historyImageQueryService.getFirstImageUrlMap(histories);
        return HistoryConverter.toHistoryMonthlyViewResult(histories,member,imageUrlMap);



    }

    @Override
    @Transactional(readOnly = true)
    public HistoryResponseDTO.HistoryDailyViewResult getDailyHistoryView(Long historyId){

        History history=historyRepository.findById(historyId)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        //List<String> hashtag와 List<String> clothes 차이
        List<String> hashtag = historyHashtagRepository.findAllByHistory_Id(historyId)//n+1발생할것임->fetchjoin hashtagtable같이 조인해서 가져오기 or hashtag repository에 jpql로 로직짜기!!
                .stream()
                .map(hc->hc.getHashtag().getName())
                .toList();


        List<String> clothes=historyClothRepository.findAllByHistory(history)
                .stream()
                .map(hc->hc.getCloth().getName())
                .toList();

        int commentCount=commentRepository.countByHistory(history);


        boolean liked= false;
        return HistoryConverter.toHistoryDailyViewResult(history,hashtag,clothes,commentCount,liked);
    }

    //코드참고함
    @Override
    public HistoryResponseDTO.HistoryCreateResult createHistory(
            HistoryRequestDTO.HistoryCreateRequest historyCreateRequest,List<MultipartFile> imageFiles
    )

    {
        Member member=memberRepository.findById(1L)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_MEMBER));
        LocalDate date;
        try{
            date= historyCreateRequest.getDate();

        } catch(Exception e){
            throw new HistoryException(ErrorStatus.INVALID_HISTORY_DATE_FORTMAT);
        }
        History history = HistoryConverter.toHistoryEntity(historyCreateRequest, member);
        historyRepository.save(history);

        if(imageFiles==null || imageFiles.isEmpty()){
            throw new HistoryException(ErrorStatus.EMPTY_HISTORY_IMAGE);
        }
        //minioUploader로 나중에 대체하기
        for(MultipartFile imageFile:imageFiles){
            HistoryImage image = HistoryImage.builder()
                    .history(history)
                    .imageUrl("지금은 url이 없음")
                    .build();
            historyImageRepository.save(image);

        }

        List<Long> clothIds=historyCreateRequest.getClothes();
        if(clothIds==null || clothIds.isEmpty()){
            throw new HistoryException(ErrorStatus.EMPTY_CLOTH);
        }
        if(clothIds.size()!= clothIds.stream().distinct().count()){
            throw new HistoryException(ErrorStatus.DUPLICATE_CLOTH);
        }
        for(Long clothId:clothIds){
            Cloth cloth = clothRepository.findById(clothId)
                    .orElseThrow(() -> new HistoryException(ErrorStatus.INVALID_CLOTH));
            cloth.increaseWearCount();
            HistoryCloth hc = HistoryCloth.builder().
                    history(history)
                    .cloth(cloth)
                    .build();
            historyClothRepository.save(hc);
        }

        List<String> hashtags=historyCreateRequest.getHashtags();
        if(hashtags==null || hashtags.isEmpty()){
            throw new HistoryException(ErrorStatus.EMPTY_HASHTAGS);
        }
        if(hashtags.size()!= hashtags.stream().distinct().count()){
            throw new HistoryException(ErrorStatus.DUPLICATE_HASHTAGS);
        }
        for(String tag:hashtags){
            Hashtag hashtag = hashtagRepository.findByName(tag)
                    .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(tag).build()));
            HashtagHistory hh = HashtagHistory.builder()
                    .history(history)
                    .hashtag(hashtag)
                    .build();
            hashtagHistoryRepository.save(hh);

        }



        return HistoryConverter.toHistoryCreateResult(history);
    }
    
    //코드참고함
    @Override
    public HistoryResponseDTO.HistoryUpdateResult updateHistory(
            Long historyId,HistoryRequestDTO.HistoryUpdateRequest historyUpdateRequest,List<MultipartFile> imageFiles
            ) {

        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_MEMBER));
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        if (!history.getMember().getId().equals(member.getId())) {
            throw new HistoryException(ErrorStatus.NO_AUTHORITY_HISTORY);
        }
        history.updateContent(historyUpdateRequest.getContent());
        if (historyUpdateRequest.getContent() != null
                && historyUpdateRequest.getContent().length() > 200) {
            throw new HistoryException(ErrorStatus.CONTENT_LENGTH_EXCEEDED);
        }
        if (imageFiles != null && imageFiles.size() > 10) {
            throw new HistoryException(ErrorStatus.TOO_MANY_IMAGES);
        }
        historyImageRepository.deleteAllByHistoryId(historyId);

//        if(imageFiles!=null && !imageFiles.isEmpty()){
//            for(MultipartFile imageFile:imageFiles){
//                String url=minioUploader.uploadImage(file);
//                historyImageRepository.save(HistoryImage.builder()
//                        .history(history)
//                        .imageUrl(url)
//                        .build());
//            }
//        } else{
//            throw new HistoryException(ErrorStatus.EMPTY_HISTORY_IMAGE);
//        }

        historyClothRepository.deleteAllByHistoryId(historyId);
        List<Long> clothes = historyUpdateRequest.getClothes();
        if (clothes.size() != clothes.stream().distinct().count()) {
            throw new HistoryException(ErrorStatus.DUPLICATE_CLOTH);
        }
        for (Long clothId : clothes) {
            Cloth cloth = clothRepository.findById(clothId)
                    .orElseThrow(() -> new HistoryException(ErrorStatus.INVALID_CLOTH));
            if (!cloth.getMember().getId().equals(member.getId())) {
                throw new HistoryException(ErrorStatus.INVALID_CLOTH);
            }
            cloth.increaseWearCount();
            historyClothRepository.save(HistoryCloth.builder()
                    .history(history)
                    .cloth(cloth)
                    .build());
        }

        hashtagHistoryRepository.deleteAllByHistoryId(historyId);
        List<String> hashtags = historyUpdateRequest.getHashtags();
        if (hashtags != null && !hashtags.isEmpty()) {
            if (hashtags.size() != hashtags.stream().distinct().count()) {
                throw new HistoryException(ErrorStatus.DUPLICATE_HASHTAGS);
            }
            for (String tag : hashtags) {
                Hashtag hashtag = hashtagRepository.findByName(tag)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(tag).build()));
                hashtagHistoryRepository.save(HashtagHistory.builder()
                        .history(history)
                        .hashtag(hashtag)
                        .build());
            }
        }
        history.updateContent(historyUpdateRequest.getContent());

        return HistoryConverter.toHistoryUpdateResult(history);

    }

    @Override
    @Transactional
    public void deleteHistory(Long historyId) {
        History history=historyRepository.findById(historyId)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        hashtagHistoryRepository.deleteAllByHistory(history);
        historyClothRepository.deleteAllByHistory(history);
        historyImageRepository.deleteAllByHistory(history);

        historyRepository.delete(history);
    }

    @Override
    @Transactional(readOnly = true)
    public HistoryResponseDTO.LikeResult likeHistory(HistoryRequestDTO.LikeRequest likeRequest){
        History history=historyRepository.findById(likeRequest.getHistoryId())
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_HISTORY));
        Member member=memberRepository.findById(1L)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_MEMBER));
        boolean isLiked=memberLikeRepository.existsByMemberAndHistory(member,history);

        if(likeRequest.isLiked()!=isLiked){
            throw new HistoryException(ErrorStatus.LIKE_STATE_MISMATCH);
        }

        if(isLiked){
            MemberLike like=memberLikeRepository.findByMemberAndHistory(member,history)
                    .orElseThrow(()->new HistoryException(ErrorStatus.LIKE_NOT_FOUND));
        }else{
            MemberLike like=MemberLike.builder()
                    .member(member)
                    .history(history)
                    .build();
            memberLikeRepository.save(like);
            history.setLikes(history.getLikes()+1);
        }

        Long likeCount=memberLikeRepository.countByHistory(history);
        boolean newState=!isLiked;


        return HistoryConverter.toHistoryLikeResult(history,newState,likeCount);

    }



    @Override
    @Transactional(readOnly = true)
    public HistoryResponseDTO.LikedUsersResult likedUser(Long historyId){
        History history=historyRepository.findById(historyId)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_HISTORY));
        //실제 로그인한 1L대신 사용자id로 써야할것
        Member member=memberRepository.findById(1L)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_MEMBER));

        List<MemberLike> likes = memberLikeRepository.findAllByHistory(history);

        //코드 참고
        List<HistoryResponseDTO.LikedUser> likedUsers=likes.stream()
                .map(like->{
                    Member likedMember=like.getMember();
                    boolean isFollowing = followRepository.existsByFollowerAndFollowing(member, likedMember);
                    boolean me=likedMember.getId().equals(member.getId());

                    return HistoryResponseDTO.LikedUser.builder()
                            .memberId(likedMember.getId())
                            .nickName(likedMember.getNickname())
                            .imageUrl(likedMember.getProfileImageUrl())
                            .followStatus(isFollowing)
                            .me(me)
                            .clokeyId(likedMember.getClokeyId())
                            .build();
                })
                .toList();

        return HistoryConverter.toLikedUsersResult(likedUsers);

    }



    @Override
    @Transactional(readOnly = true)
    public HistoryResponseDTO.writeCommentResult writeComment(
            Long historyId,HistoryRequestDTO.WriteCommentRequest request
    ){

        History history=historyRepository.findById(historyId)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        Member member=memberRepository.findById(1L)
                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_MEMBER));


        String content=request.getContent();
        if(content==null || content.isEmpty()){
            throw new HistoryException(ErrorStatus.COMMENT_CONTENT_EMPTY);
        }
        if(content.length()>50){
            throw new HistoryException(ErrorStatus.COMMENT_CONTENT_TOO_LONG);
        }

        Comment newComment=Comment.builder()
                    .content(content)
                    .history(history)
                    .member(member)
                    .history(history)
                    .build();
        commentRepository.save(newComment);
        return HistoryConverter.toHistoryWriteCommentResult(newComment);

    }





















}

