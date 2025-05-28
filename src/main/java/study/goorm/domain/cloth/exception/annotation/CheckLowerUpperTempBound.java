package study.goorm.domain.cloth.exception.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import study.goorm.domain.cloth.exception.validator.CheckLowerUpperTempBoundValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckLowerUpperTempBoundValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER}) // ElementType.TYPE 이걸 붙이면 클래스 위에도 저렇게 붙일 수 있음
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLowerUpperTempBound {

    String message() default "상한 온도는 하한 온도보다 높아야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
