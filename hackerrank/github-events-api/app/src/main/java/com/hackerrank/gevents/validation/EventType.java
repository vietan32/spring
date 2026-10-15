package com.hackerrank.gevents.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;

@Documented
@Constraint(validatedBy = EventTypeValidator.class)
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventType {

    String message() default "Invalid event type. Allowed values: PushEvent, ReleaseEvent, WatchEvent";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

