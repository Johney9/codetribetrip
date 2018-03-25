package trip.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DestinationValidator.class})
public @interface DestinationExists {
    String message() default "Entered destination does not exist on Google Maps.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
