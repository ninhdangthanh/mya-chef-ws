package vn.com.ids.myachef.business.validation.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Documented
@Retention(RUNTIME)
@Target(value = { METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
public @interface Version {
    String message() default "Invalid format.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    
    @Pattern(regexp = "(\\d*)\\.(\\d+)\\.(\\d+)")
    String value() default "";
}
