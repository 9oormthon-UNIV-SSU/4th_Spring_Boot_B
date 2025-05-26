package study.goorm.domain.cloth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.cloth.converter.ClothConverter;
import study.goorm.domain.cloth.domain.entity.Category;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.cloth.domain.entity.ClothImage;
import study.goorm.domain.cloth.domain.repository.CategoryRepository;
import study.goorm.domain.cloth.domain.repository.ClothImageRepository;
import study.goorm.domain.cloth.domain.repository.ClothRepository;
import study.goorm.domain.cloth.dto.ClothRequestDTO;
import study.goorm.domain.cloth.dto.ClothResponseDTO;
import study.goorm.domain.cloth.exception.ClothException;
import study.goorm.domain.folder.domain.repository.ClothFolderRepository;
import study.goorm.domain.history.domain.repository.HistoryClothRepository;
import study.goorm.domain.member.domain.entity.Member;
import study.goorm.domain.member.domain.repository.MemberRepository;
import study.goorm.domain.member.exception.MemberException;
import study.goorm.domain.model.Enum.ClothSort;
import study.goorm.global.error.code.status.ErrorStatus;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClothServiceImpl implements ClothService {

    private final ClothRepository clothRepository;
    private final ClothImageRepository clothImageRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ClothFolderRepository clothFolderRepository;
    private final HistoryClothRepository historyClothRepository;
    private final ClothImageQueryService clothImageQueryService;

    @Override
    @Transactional(readOnly = true)
    public ClothResponseDTO.ClothEditViewDTO getClothEditView(Long clothId) {
        Cloth cloth = clothRepository.findById(clothId)
                .orElseThrow(()-> new ClothException(ErrorStatus.NO_SUCH_CLOTH));

        List<ClothImage> clothImageUrls = clothImageRepository.findAllByCloth(cloth);

        String firstImageUrl = clothImageUrls.stream()
                .findFirst()
                .map(ClothImage::getUrl)
                .orElseThrow(() -> new ClothException(ErrorStatus.NO_ClOTH_IMAGE));

        return ClothConverter.toClothEditViewDTO(cloth, firstImageUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public ClothResponseDTO.MemberClosetDTO getMemberCloset(String clokeyId, ClothSort sort, int page, int size) {
        Member member = memberRepository.findByClokeyId(clokeyId)
                .orElseThrow(()-> new MemberException(ErrorStatus.NO_SUCH_MEMBER));
        PageRequest pageRequest = PageRequest.of(page,size);

        Page<Cloth> clothes;

        if (sort.equals(ClothSort.LATEST)){
            clothes = clothRepository.findByMemberOrderByCreatedAtDesc(member, pageRequest);
        }else if(sort.equals(ClothSort.OLDEST)){
            clothes = clothRepository.findByMemberOrderByCreatedAtAsc(member,pageRequest);
        }else if(sort.equals(ClothSort.WEAR)){
            clothes = clothRepository.findByMemberOrderByWearNumberDesc(member,pageRequest);
        }else {
            clothes = clothRepository.findByMemberOrderByWearNumberAsc(member,pageRequest);
        }

        Map<Long, String> firstImagesOfCloth = clothImageQueryService.getFirstImageUrlMap(clothes);

        return ClothConverter.toMemberClosetDTO(member,firstImagesOfCloth,clothes);
    }

    @Override
    @Transactional
    public ClothResponseDTO.ClothCreateDTO createCloth(ClothRequestDTO.ClothCreateDTO clothCreateDTO, MultipartFile image) {
        Member member = memberRepository.findById(clothCreateDTO.getMemberId())
                .orElseThrow(()-> new MemberException(ErrorStatus.NO_SUCH_MEMBER));

        Category category = categoryRepository.findById(clothCreateDTO.getCategoryId())
                .orElseThrow(()-> new ClothException(ErrorStatus.NO_SUCH_CATEGORY));

        Cloth newCloth = Cloth.builder()
                .name(clothCreateDTO.getName())
                .wearNumber(0)
                .season(clothCreateDTO.getSeasons())
                .tempUpperBound(clothCreateDTO.getTempUpperBound())
                .tempLowerBound(clothCreateDTO.getTempLowerBound())
                .thicknessLevel(clothCreateDTO.getThicknessLevel())
                .clothUrl(clothCreateDTO.getClothUrl())
                .brand(clothCreateDTO.getBrand())
                .category(category)
                .member(member)
                .build();

        clothRepository.save(newCloth);

        ClothImage newClothImage = ClothImage.builder()
                .cloth(newCloth)
                .url("아직 S3를 구현하지 않아서 url이 없어용")
                .build();

        clothImageRepository.save(newClothImage);

        return ClothConverter.toClothCreateDTO(newCloth);

    }

    @Override
    @Transactional
    public void deleteCloth(Long clothId) {

        Cloth cloth = clothRepository.findById(clothId)
                .orElseThrow(()-> new ClothException(ErrorStatus.NO_SUCH_CLOTH));

        //매핑 테이블 삭제
        clothImageRepository.deleteAllByCloth(cloth);
        clothFolderRepository.deleteAllByCloth(cloth);
        historyClothRepository.deleteAllByCloth(cloth);

        //최종 옷 삭제
        clothRepository.delete(cloth);
    }
}
