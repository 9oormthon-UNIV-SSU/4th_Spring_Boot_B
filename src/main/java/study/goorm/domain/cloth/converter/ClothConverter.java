package study.goorm.domain.cloth.converter;

import lombok.Getter;
import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.cloth.dto.ClothResponseDTO;

@Getter
public class ClothConverter {
    public static ClothResponseDTO.ClothEditViewResult toClothEditViewResult(Cloth cloth, String clothImageUrl) {
        return ClothResponseDTO.ClothEditViewResult.builder()
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


    public static ClothResponseDTO.ClothCreateResult toClothCreateResult(Cloth cloth){
        return ClothResponseDTO.ClothCreateResult.builder()
                .id(cloth.getId())
                .build();
    }
}
