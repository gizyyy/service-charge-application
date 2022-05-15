package com.digitalcharging.application.service.dto;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChargeHistory {
	private Long transactionId;

	@NotNull(message = "The vehicle is required.")
	@Positive
	private Long vehicleId;

	@Valid
	private TransactionRange transactionRange;

	@Positive(message = "Cost should be bigger than zero.")
	private BigDecimal cost;
}
