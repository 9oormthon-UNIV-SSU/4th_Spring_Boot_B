package study.goorm.domain.cloth.converter;

import study.goorm.domain.cloth.domain.entity.Cloth;
import study.goorm.domain.cloth.dto.ClothResponseDTO;

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
}
