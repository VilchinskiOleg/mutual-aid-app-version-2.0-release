package order.orderservice.util.validation.validator;

import static java.util.Objects.isNull;
import static java.util.regex.Pattern.compile;

import order.orderservice.util.validation.annotation.ValidOrderPriority;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderPriorityValidator implements ConstraintValidator<ValidOrderPriority, String> {

    private Pattern pattern;

    @Override
    public void initialize(ValidOrderPriority constraintAnnotation) {
        var patternStr = constraintAnnotation.pattern();
        pattern = compile(patternStr);
    }

    @Override
    public boolean isValid(String priority, ConstraintValidatorContext context) {
        if (isNull(priority)) {
            return true;
        }
        Matcher matcher = pattern.matcher(priority);
        return matcher.matches();
    }
}
