package study.goorm.domain.cloth.application;

import study.goorm.domain.cloth.dto.ClothResponseDTO;

public interface ClothService {

    ClothResponseDTO.ClothEditViewDTO getClothEditView(Long clothId);

}
