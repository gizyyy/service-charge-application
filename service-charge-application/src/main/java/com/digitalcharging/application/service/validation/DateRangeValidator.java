package com.digitalcharging.application.service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.digitalcharging.application.service.dto.TransactionRange;

public class DateRangeValidator implements ConstraintValidator<ValidTransactionRange, TransactionRange> {

	public void initialize(ValidTransactionRange constraintAnnotation) {

	}

	@Override
	public boolean isValid(TransactionRange range, ConstraintValidatorContext constraintValidatorContext) {
		if (range.getStartTime().isAfter(range.getEndTime())) {
			return false;
		}
		return true;
	}
}