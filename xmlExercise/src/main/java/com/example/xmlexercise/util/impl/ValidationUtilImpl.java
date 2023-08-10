package com.example.xmlexercise.util.impl;

import com.example.xmlexercise.util.ValidationUtil;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtilImpl implements ValidationUtil {

    private final Validator validator;

    public ValidationUtilImpl() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <T> boolean isValid(T entity) {
        return validator.validate(entity).isEmpty();
    }
}
