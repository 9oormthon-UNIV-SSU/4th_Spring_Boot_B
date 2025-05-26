package study.goorm.domain.cloth.converter;

import org.springframework.data.domain.Page;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.cloth.dto.ClothResponseDTO;
import study.goorm.domain.member.domain.entity.Member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClothConverter {

    public static ClothResponseDTO.ClothEditViewDTO toClothEditViewDTO(Cloth cloth, String clothImageUrl){
        return ClothResponseDTO.ClothEditViewDTO.builder()
                .id(cloth.getId())
                .brand(cloth.getBrand())
                .categoryId(cloth.getCategory().getId())
                .clothUrl(cloth.getClothUrl())
                .imageUrl(clothImageUrl)
                .name(cloth.getName())
                .seasons(cloth.getSeason())
                .tempLowerBound(cloth.getTempLowerBound())
                .tempUpperBound(cloth.getTempUpperBound())
                .thicknessLevel(cloth.getThicknessLevel())
                .build();
    }

    public static ClothResponseDTO.MemberClosetDTO toMemberClosetDTO(Member member, Map<Long,String> firstImagesOfCloth, Page<Cloth> clothes){
        return ClothResponseDTO.MemberClosetDTO.builder()
                .nickName(member.getNickname())
                .clothPreviewListDTO(toClothPreviewListDTO(firstImagesOfCloth, clothes))
                .build();
    }

    private static ClothResponseDTO.ClothPreviewListDTO toClothPreviewListDTO(Map<Long, String> firstImagesOfCloth, Page<Cloth> clothes){
        return ClothResponseDTO.ClothPreviewListDTO.builder()
                .clothPreviews(toClothPreviewDTO(firstImagesOfCloth, clothes))
                .isFirst(clothes.isFirst())
                .isLast(clothes.isLast())
                .totalElements(clothes.getTotalElements())
                .totalPage(clothes.getTotalPages())
                .build();
    }

    private static List<ClothResponseDTO.ClothPreviewDTO> toClothPreviewDTO(Map<Long, String> firstImagesOfCloth, Page<Cloth> clothes){
        return clothes.stream()
                .map(cloth -> ClothResponseDTO.ClothPreviewDTO.builder()
                        .id(cloth.getId())
                        .name(cloth.getName())
                        .wearNum(cloth.getWearNumber())
                        .imageUrl(firstImagesOfCloth.get(cloth.getId()))
                        .build())
                .collect(Collectors.toList());
    }
}
