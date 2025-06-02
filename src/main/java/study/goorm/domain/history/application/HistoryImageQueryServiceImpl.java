package study.goorm.domain.history.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.goorm.domain.history.domain.entity.History;
import study.goorm.domain.history.domain.entity.HistoryImage;
import study.goorm.domain.history.domain.repository.HistoryImageRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryImageQueryServiceImpl implements HistoryImageQueryService {

    private final HistoryImageRepository historyImageRepository;

    public Map<Long, String> getFirstImageUrlMap(List<History> histories) {
        List<Long> historyIds = histories.stream()
                .map(History::getId)
                .toList();

        List<HistoryImage> firstImages = historyImageRepository.findFirstImagesByHistoryIds(historyIds);

        return firstImages.stream()
                .collect(Collectors.toMap(
                        image -> image.getHistory().getId(),
                        HistoryImage::getUrl
                ));
    }
}
