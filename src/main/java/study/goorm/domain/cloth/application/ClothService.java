package study.goorm.domain.cloth.application;

import org.springframework.web.multipart.MultipartFile;
import study.goorm.domain.cloth.dto.ClothRequestDTO;
import study.goorm.domain.cloth.dto.ClothResponseDTO;
import study.goorm.domain.model.Enum.ClothSort;

public interface ClothService {

    ClothResponseDTO.ClothEditViewDTO getClothEditView(Long clothId);
    ClothResponseDTO.MemberClosetDTO getMemberCloset(String clokeyId, ClothSort sort, int page, int size);
    ClothResponseDTO.ClothCreateDTO createCloth(ClothRequestDTO.ClothCreateDTO clothCreateDTO, MultipartFile image);
}


