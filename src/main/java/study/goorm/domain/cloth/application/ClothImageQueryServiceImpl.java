package study.goorm.domain.cloth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.cloth.domain.entity.ClothImage;
import study.goorm.domain.cloth.domain.repository.ClothImageRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ClothImageQueryServiceImpl implements ClothImageQueryService {
    private final ClothImageRepository clothImageRepository;

    @Override
    public Map<Long,String> getFirstImageUrlMap(Iterable<Cloth> clothes){
        List<Long> clothIds= StreamSupport.stream(clothes.spliterator(),false)
                .map(Cloth::getId)
                .toList();

        List<ClothImage> firstImages=clothImageRepository.findFirstImagesByClothIds(clothIds);

        return firstImages.stream()
                .collect(Collectors.toMap(
                        image->image.getCloth().getId(),
                        ClothImage::getImageUrl
                ));

    }
}
