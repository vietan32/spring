package com.hackerrank.gevents.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class EventTypeValidator implements ConstraintValidator<EventType, String> {

    private static final List<String> VALID_TYPES = Arrays.asList(
            "PushEvent",
            "ReleaseEvent",
            "WatchEvent"
    );

    @Override
    public void initialize(EventType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return VALID_TYPES.contains(value);
    }
}
