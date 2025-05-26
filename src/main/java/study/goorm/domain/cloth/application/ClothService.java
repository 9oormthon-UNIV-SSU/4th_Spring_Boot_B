package study.goorm.domain.cloth.application;

import study.goorm.domain.cloth.dto.ClothResponseDTO;
import study.goorm.domain.model.Enum.ClothSort;

public interface ClothService {

    ClothResponseDTO.ClothEditViewDTO getClothEditView(Long clothId);
    ClothResponseDTO.MemberClosetDTO getMemberCloset(String clokeyId, ClothSort sort, int page, int size);

}
