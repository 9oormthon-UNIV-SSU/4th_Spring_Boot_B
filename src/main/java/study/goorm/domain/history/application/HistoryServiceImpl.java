package study.goorm.domain.history.application;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;
import study.goorm.domain.history.converter.HistoryConverter;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.repository.HistoryRepository;
import study.goorm.domain.history.dto.HistoryResponseDTO;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.member.domain.exception.MemberException;
import study.goorm.domain.member.domain.repository.MemberRepository;
import study.goorm.global.error.code.status.ErrorStatus;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;


    @Override
    public HistoryResponseDTO.MonthlyHistoryViewResult getMonthlyHistories(String clokeyId, YearMonth month) {
        YearMonth yearMonth = YearMonth.from(month);

        if (clokeyId == null) {
//            clokeyId = SecurityUtil.getCurrentClokeyId();
        }

        Member member = memberRepository.findByClokeyId(clokeyId)
                .orElseThrow(() -> new MemberException(ErrorStatus.NO_SUCH_MEMBER));

        List<History> histories = historyRepository.findByClokeyIdAndMonth(
                clokeyId, yearMonth.getYear(), yearMonth.getMonthValue()
        );

        List<HistoryResponseDTO.HistoryItem> historyDTOs = histories.stream()
                .map(history -> {
                    String imageUrl = (history.getPhotos() == null || history.getPhotos().isEmpty())
                            ? null
                            : Optional.ofNullable(history.getPhotos().get(0).getImageUrl()).orElse("비공개입니다");

                    return HistoryConverter.toHistoryItem(history, imageUrl);
                })
                .collect(Collectors.toList());

        return HistoryConverter.toMonthlyHistoryViewResult(member, historyDTOs);
    }


}

//public class SecurityUtil {
//
//    public static String getCurrentClokeyId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
////        if (authentication == null || !authentication.isAuthenticated()) {
////            throw new MemberException(ErrorStatus.UNAUTHORIZED);
////        }
//
//        return authentication.getName(); // 또는 ((CustomUserDetails) authentication.getPrincipal()).getClokeyId();
//    }
//}

