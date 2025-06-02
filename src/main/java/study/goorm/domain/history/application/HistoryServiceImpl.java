package study.goorm.domain.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.cloth.domain.repository.ClothRepository;
import study.goorm.domain.cloth.exception.ClothException;
import study.goorm.domain.history.domain.entity.*;
import study.goorm.domain.history.domain.repository.*;
import study.goorm.domain.history.dto.HistoryRequestDTO;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.history.exception.HistoryException;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.member.domain.repository.MemberRepository;
import study.goorm.global.error.code.status.ErrorStatus;
import study.goorm.global.service.ImageUploadService;
import study.goorm.domain.model.enums.Visibility;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final ClothRepository clothRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagHistoryRepository hashtagHistoryRepository;
    private final HistoryImageRepository historyImageRepository;
    private final HistoryClothRepository historyClothRepository;
    private final MemberRepository memberRepository;
    private final ImageUploadService imageUploadService;
    private final CommentRepository commentRepository;
    private final MemberLikeRepository memberLikeRepository;

    @Override
    @Transactional
    public HistoryResponseDTO.HistoryUpdateResult updateHistory(Long historyId,
                                                                HistoryRequestDTO.HistoryUpdateRequest historyUpdateRequest,
                                                                List<MultipartFile> images,
                                                                Long memberId) {

        // 1. 기록 존재 여부 및 권한 확인
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        if (!history.getMember().getId().equals(memberId)) {
            throw new HistoryException(ErrorStatus.HISTORY_FORBIDDEN);
        }

        // 2. 요청 데이터 검증
        validateUpdateRequest(historyUpdateRequest, memberId, images);

        // 3. 이전 착용 횟수 감소 (기존 옷들)
        decreaseWearNumForPreviousClothes(historyId);

        // 4. 기록 기본 정보 업데이트
        history.updateHistory(historyUpdateRequest.getContent(), historyUpdateRequest.getVisibility());

        // 5. 기존 연관 데이터 삭제
        deleteExistingRelations(historyId);

        // 6. 새로운 옷 연관 관계 생성 및 착용 횟수 증가
        createNewClothRelations(historyId, historyUpdateRequest.getClothes());

        // 7. 새로운 해시태그 처리
        processHashtags(historyId, historyUpdateRequest.getHashtags());

        // 8. 이미지 업데이트
        updateImages(historyId, images);

        return HistoryResponseDTO.HistoryUpdateResult.builder().build();
    }

    private void validateUpdateRequest(HistoryRequestDTO.HistoryUpdateRequest request, Long memberId, List<MultipartFile> images) {
        // content 길이 검증
        if (request.getContent() != null && request.getContent().length() > 200) {
            throw new HistoryException(ErrorStatus.HISTORY_CONTENT_TOO_LONG);
        }

        // 옷 중복 검증
        Set<Long> uniqueClothes = new HashSet<>(request.getClothes());
        if (uniqueClothes.size() != request.getClothes().size()) {
            throw new HistoryException(ErrorStatus.HISTORY_DUPLICATE_CLOTHES);
        }

        // 해시태그 중복 검증
        Set<String> uniqueHashtags = new HashSet<>(request.getHashtags());
        if (uniqueHashtags.size() != request.getHashtags().size()) {
            throw new HistoryException(ErrorStatus.HISTORY_DUPLICATE_HASHTAGS);
        }

        // 내 옷장의 옷인지 검증
        List<Cloth> memberClothes = clothRepository.findAllByIdInAndMemberId(request.getClothes(), memberId);
        if (memberClothes.size() != request.getClothes().size()) {
            throw new HistoryException(ErrorStatus.HISTORY_INVALID_CLOTHES);
        }

        // 이미지 개수 검증
        if (images == null || images.isEmpty()) {
            throw new HistoryException(ErrorStatus.HISTORY_IMAGE_REQUIRED);
        }
        if (images.size() > 10) {
            throw new HistoryException(ErrorStatus.HISTORY_TOO_MANY_IMAGES);
        }
    }

    private void decreaseWearNumForPreviousClothes(Long historyId) {
        List<HistoryCloth> previousHistoryClothes = historyClothRepository.findAllByHistoryId(historyId);

        for (HistoryCloth historyCloth : previousHistoryClothes) {
            Cloth cloth = historyCloth.getCloth();
            cloth.decreaseWearNum();
        }
    }

    private void deleteExistingRelations(Long historyId) {
        // 기존 옷-기록 연관관계 삭제
        historyClothRepository.deleteAllByHistoryId(historyId);

        // 기존 해시태그-기록 연관관계 삭제
        hashtagHistoryRepository.deleteAllByHistoryId(historyId);

        // 기존 이미지 삭제
        historyImageRepository.deleteAllByHistoryId(historyId);
    }

    private void createNewClothRelations(Long historyId, List<Long> clothIds) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        for (Long clothId : clothIds) {
            Cloth cloth = clothRepository.findById(clothId)
                    .orElseThrow(() -> new ClothException(ErrorStatus.NO_SUCH_CLOTH));

            // 착용 횟수 증가
            cloth.increaseWearNum();

            // 새로운 연관관계 생성
            HistoryCloth historyCloth = HistoryCloth.builder()
                    .history(history)
                    .cloth(cloth)
                    .build();

            historyClothRepository.save(historyCloth);
        }
    }

    private void processHashtags(Long historyId, List<String> hashtagNames) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        for (String hashtagName : hashtagNames) {
            // 해시태그가 존재하지 않으면 생성
            Hashtag hashtag = hashtagRepository.findByName(hashtagName)
                    .orElseGet(() -> {
                        Hashtag newHashtag = Hashtag.builder()
                                .name(hashtagName)
                                .build();
                        return hashtagRepository.save(newHashtag);
                    });

            // 해시태그-기록 연관관계 생성
            HashtagHistory hashtagHistory = HashtagHistory.builder()
                    .hashtag(hashtag)
                    .history(history)
                    .build();

            hashtagHistoryRepository.save(hashtagHistory);
        }
    }

    private void updateImages(Long historyId, List<MultipartFile> images) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        // 새로운 이미지들 저장
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                // MinIO를 통한 실제 이미지 업로드
                String imageUrl = imageUploadService.uploadImage(image);

                HistoryImage historyImage = HistoryImage.builder()
                        .imageUrl(imageUrl)
                        .history(history)
                        .build();

                historyImageRepository.save(historyImage);
            }
        }
    }

    @Override
    @Transactional
    public void deleteHistory(Long historyId, Long memberId) {

        // 1. 기록 존재 여부 및 권한 확인
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new HistoryException(ErrorStatus.NO_SUCH_HISTORY));

        if (!history.getMember().getId().equals(memberId)) {
            throw new HistoryException(ErrorStatus.HISTORY_FORBIDDEN);
        }

        // 2. 기록에 포함된 옷들의 착용 횟수 감소
        decreaseWearNumForPreviousClothes(historyId);

        // 3. 연관된 모든 데이터 삭제
        deleteAllRelatedData(historyId);

        // 4. 기록 자체 삭제
        historyRepository.delete(history);
    }

    private void deleteAllRelatedData(Long historyId) {
        // 댓글 삭제
        commentRepository.deleteAllByHistoryId(historyId);

        // 좋아요 삭제
        memberLikeRepository.deleteAllByHistoryId(historyId);

        // 해시태그-기록 연관관계 삭제 (해시태그 자체는 유지)
        hashtagHistoryRepository.deleteAllByHistoryId(historyId);

        // 기록-옷 연관관계 삭제
        historyClothRepository.deleteAllByHistoryId(historyId);

        // 기록 이미지 삭제
        historyImageRepository.deleteAllByHistoryId(historyId);
    }
}