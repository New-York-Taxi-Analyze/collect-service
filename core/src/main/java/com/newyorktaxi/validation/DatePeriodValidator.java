package com.newyorktaxi.validation;

import com.newyorktaxi.model.DatePeriod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatePeriodValidator implements ConstraintValidator<DatePeriodConstraint, DatePeriod> {

    @Override
    public void initialize(DatePeriodConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DatePeriod datePeriod, ConstraintValidatorContext context) {
        if(datePeriod.getDay() != null) {
            if (datePeriod.getMonth() == null) {
                context.buildConstraintViolationWithTemplate("value is empty")
                        .addPropertyNode("month")
                        .addConstraintViolation();
                return false;
            } else if (datePeriod.getYear() == null) {
                context.buildConstraintViolationWithTemplate("value is empty")
                        .addPropertyNode("year")
                        .addConstraintViolation();
                return false;
            }
        }

        if (datePeriod.getYear() != null && datePeriod.getMonth() != null && datePeriod.getDay() != null) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sdf.setLenient(false);
            try {
                sdf.parse(datePeriod.getYear() + "/" + datePeriod.getMonth() + "/" + datePeriod.getDay());
            } catch (ParseException e) {
                context.buildConstraintViolationWithTemplate("date is invalid")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
        return false;
    }
}
