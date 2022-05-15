package com.digitalcharging.application.service.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.digitalcharging.application.service.validation.ValidTransactionRange;
import com.digitalcharging.application.utils.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

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
@ValidTransactionRange
public class TransactionRange {

	@NotNull(message = "The chage start time is required.")
	@PastOrPresent(message = "The chage start time must be in the past or now.")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime startTime;

	@NotNull(message = "The chage end time is required.")
	@PastOrPresent(message = "The chage end time must be in the past or now.")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime endTime;
}
