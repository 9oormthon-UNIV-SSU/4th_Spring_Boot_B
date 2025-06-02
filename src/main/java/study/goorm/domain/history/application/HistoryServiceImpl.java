package study.goorm.domain.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.cloth.application.ClothImageQueryService;
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

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final HashtagHistoryRepository hashtagHistoryRepository;
    private final HistoryImageQueryService historyImageQueryService;
    private final HistoryImageRepository historyImageRepository;
    private final HistoryClothRepository historyClothRepository;
    private final ClothRepository clothRepository;
    private final HashtagRepository hashtagRepository;
    private final ClothImageQueryService clothImageQueryService;

    private final MinioUploader minioUploader;

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public HistoryResponseDTO.DailyHistoryDTO getDailyHistory(Long historyId) {
        History history = historyRepository.findByIdWithMember(historyId)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        Map<Long, String> imageUrlMap = historyImageQueryService.getFirstImageUrlMap(List.of(history));

        List<String> hashtags = hashtagHistoryRepository.findByHistoryId(historyId)
                .stream()
                .map(hashtagHistory -> hashtagHistory.getHashtag().getName())
                .toList();

        List<HistoryCloth> historyCloths = historyClothRepository.findAllByHistoryId(historyId);
        List<Cloth> clothes = historyCloths.stream()
                .map(HistoryCloth::getCloth)
                .toList();

        Map<Long, String> clothImageMap = clothImageQueryService.getFirstImageUrlMap(clothes);

        return HistoryConverter.toDailyHistoryDTO(
                history,
                imageUrlMap,
                hashtags,
                clothes,
                clothImageMap,
                false,     // liked (로그인 사용자 기반 로직 추가 가능)
                0          // commentCount (댓글 기능 도입 시 확장 가능)
        );
    }

    @Override
    @Transactional
    public HistoryResponseDTO.CreateHistoryResultDTO createHistory(HistoryRequestDTO.CreateHistoryDTO requestDTO, List<MultipartFile> imageFiles){
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY_MEMBER));

        LocalDate date;
        try {
            date = requestDTO.getDate();
        } catch (Exception e) {
            throw new HistoryException(ErrorStatus.INVALID_HISTORY_DATE_FORMAT);
        }

        History history = HistoryConverter.toHistoryEntity(requestDTO, member);
        historyRepository.save(history);

        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new HistoryException(ErrorStatus.EMPTY_HISTORY_IMAGE);
        }
        for (MultipartFile imageFile : imageFiles) {
            String imageUrl = minioUploader.uploadImage(imageFile);
            HistoryImage image = HistoryImage.builder()
                    .history(history)
                    .url(imageUrl)
                    .build();
            historyImageRepository.save(image);
        }

        List<Long> clothIds = requestDTO.getClothes();
        if (clothIds == null || clothIds.isEmpty()) {
            throw new HistoryException(ErrorStatus.EMPTY_CLOTH);
        }
        if (clothIds.size() != clothIds.stream().distinct().count()) {
            throw new HistoryException(ErrorStatus.DUPLICATE_CLOTH);
        }
        for (Long clothId : clothIds) {
            Cloth cloth = clothRepository.findById(clothId)
                    .orElseThrow(() -> new HistoryException(ErrorStatus.INVALID_CLOTH_ID));
            // 착용 횟수 증가
            cloth.increaseWearCount();
            HistoryCloth hc = HistoryCloth.builder()
                    .history(history)
                    .cloth(cloth)
                    .build();
            historyClothRepository.save(hc);
        }

        List<String> hashtags = requestDTO.getHashtags();
        if (hashtags != null && !hashtags.isEmpty()) {
            if (hashtags.size() != hashtags.stream().distinct().count()) {
                throw new HistoryException(ErrorStatus.DUPLICATE_HASHTAG);
            }
            for (String tag : hashtags) {
                Hashtag hashtag = hashtagRepository.findByName(tag)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(tag).build()));
                HashtagHistory hh = HashtagHistory.builder()
                        .history(history)
                        .hashtag(hashtag)
                        .build();
                hashtagHistoryRepository.save(hh);
            }
        }

        return HistoryConverter.toCreateHistoryResultDTO(history);
    }
}
