package order.orderservice.rest.validation.validator;

import static java.util.Objects.isNull;
import static java.util.regex.Pattern.compile;

import order.orderservice.rest.validation.annotation.ValidOrderType;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderTypeValidator implements ConstraintValidator<ValidOrderType, String> {

    private Pattern pattern;

    @Override
    public void initialize(ValidOrderType constraintAnnotation) {
        var patternStr = constraintAnnotation.pattern();
        pattern = compile(patternStr);
    }

    @Override
    public boolean isValid(String type, ConstraintValidatorContext constraintValidatorContext) {
        if (isNull(type)) {
            return true;
        }
        Matcher matcher = pattern.matcher(type);
        return matcher.matches();
    }
}
