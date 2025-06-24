package study.goorm.domain.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.history.converter.HistoryConverter;
import study.goorm.domain.history.domain.entity.Hashtag;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.repository.*;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.history.exception.HistoryException;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.member.domain.repository.MemberRepository;
import study.goorm.domain.model.enums.Visibility;
import study.goorm.global.error.code.status.ErrorStatus;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final HashtagRepository hashtagRepository;
    private final CommentRepository commentRepository;
    private final HistoryClothRepository historyClothRepository;
    private final HashtagHistoryRepository historyHashtagRepository;
    private final HashtagHistoryRepository hashtagHistoryRepository;
    private final HistoryImageRepository historyImageRepository;
    private final MemberRepository memberRepository;
    private final HistoryImageQueryService historyImageQueryService;


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
        List<String> hashtag = historyHashtagRepository.findAllByHistory_Id(historyId)
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

    //매우 어렵다
    @Override
    public HistoryResponseDTO.HistoryCreateResult createHistory(
            HistoryRequestDTO.HistoryCreateRequest historyCreateResult
    )
    {

        return null;
    }
    
    //매우 어렵다
    @Override
    public HistoryResponseDTO.HistoryUpdateResult updateHistory(
            HistoryRequestDTO.HistoryUpdateRequest historyUpdateResult
            )
    {
//        History history=historyRepository.findById(historyUpdateResult.getHistoryId())
//                .orElseThrow(()->new HistoryException(ErrorStatus.NO_SUCH_HISTORY));
//        History newHistory=History.builder()
//                .content(history.getContent())
//                .clothes(cloth.getName())
//                .hashtags(hashtag.getName())
//                .visibility(visibility)
//                .build();
//        historyRepository.save(newHistory);
//        return HistoryConverter.toHistoryUpdateResult(newHistory);
        return null;
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

}

