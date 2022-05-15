package com.digitalcharging.application.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "charge_history")
public class ChargeHistoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Long transactionId;
	@NonNull
	@Column
	private Long vehicleId;
	@NonNull
	@Convert(converter = LocalDateTimeConverter.class)
	@Column
	private LocalDateTime startTime;
	@NonNull
	@Convert(converter = LocalDateTimeConverter.class)
	@Column
	private LocalDateTime endTime;
	@NonNull
	@Column
	private BigDecimal cost;
}
