package mate.academy.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String fieldName;
    private String matchFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.fieldName = constraintAnnotation.field();
        this.matchFieldName = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
        Object fieldValue = wrapper.getPropertyValue(fieldName);
        Object matchValue = wrapper.getPropertyValue(matchFieldName);
        return Objects.equals(fieldValue, matchValue);
    }
}
