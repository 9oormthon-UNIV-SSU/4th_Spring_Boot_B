package study.goorm.domain.model.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.goorm.domain.cloth.dto.ClothRequestDTO;
import study.goorm.domain.model.exception.annotation.CheckLowerUpperTempBound;
import study.goorm.global.error.code.status.ErrorStatus;

@Component
@RequiredArgsConstructor
public class CheckLowerUpperTempBoundValidator implements ConstraintValidator<CheckLowerUpperTempBound, Object> {

    @Override
    public void initialize(CheckLowerUpperTempBound constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true; // null은 유효하다고 간주
        }

        Integer tempLowerBound = null;
        Integer tempUpperBound = null;

        // 타입에 따라 분기 처리
        if (obj instanceof ClothRequestDTO.ClothCreateRequest) {
            var request = (ClothRequestDTO.ClothCreateRequest) obj;
            tempLowerBound = request.getTempLowerBound();
            tempUpperBound = request.getTempUpperBound();
        } else if (obj instanceof ClothRequestDTO.ClothUpdateRequest) {
            var request = (ClothRequestDTO.ClothUpdateRequest) obj;
            tempLowerBound = request.getTempLowerBound();
            tempUpperBound = request.getTempUpperBound();
        } else {
            // 다른 타입이면 검증하지 않음
            return true;
        }

        // PATCH 특성: 둘 중 하나가 null이면 검증하지 않음
        if (tempLowerBound == null || tempUpperBound == null) {
            return true;
        }

        boolean isValid = tempLowerBound <= tempUpperBound;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.LOWER_TEMP_BIGGER_THAN_UPPER_TEMP.getMessage()).addConstraintViolation();
        }

        return isValid;
    }
}