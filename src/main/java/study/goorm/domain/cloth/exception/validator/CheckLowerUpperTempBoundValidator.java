package study.goorm.domain.cloth.exception.validator;

import jakarta.validation.ConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.goorm.domain.cloth.dto.ClothRequestDTO;
import study.goorm.domain.cloth.exception.annotation.CheckLowerUpperTempBound;

@Component
@RequiredArgsConstructor
public class CheckLowerUpperTempBoundValidator {

    @Override
    public void initialize(CheckLowerUpperTempBound constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ClothRequestDTO.ClothCreateRequest request, ConstraintValidatorContext context) {
        boolean isValid = request.getTempLowerBound() <= request.getTempUpperBound();

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.LOWER_TEMP_BIGGER_THAN_UPPER_TEMP.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
