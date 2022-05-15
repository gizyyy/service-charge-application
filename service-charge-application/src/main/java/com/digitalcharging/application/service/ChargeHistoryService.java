package com.digitalcharging.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitalcharging.application.persistence.ChargeHistoryEntity;
import com.digitalcharging.application.persistence.ChargeHistoryRepository;
import com.digitalcharging.application.service.dto.ChargeHistory;

@Service
public class ChargeHistoryService {

	@Autowired
	private ChargeHistoryRepository chargeHistoryRepository;

	@Autowired
	private ChargeHistoryMapper chargeHistoryMapper;

	public List<ChargeHistory> getHistoryForVehicle(final Long vehicleId, final boolean sortStartDate,
			final boolean sortEndDate) {

		List<ChargeHistoryEntity> transactions = findByVehicle(vehicleId, sortStartDate, sortEndDate);

		if (CollectionUtils.isEmpty(transactions)) {
			return null;
		}

		return transactions.stream().map(t -> chargeHistoryMapper.mapFromEntity(t)).collect(Collectors.toList());
	}

	public Optional<ChargeHistory> getHistoryWithId(final Long transactionId) {
		Optional<ChargeHistoryEntity> transaction = chargeHistoryRepository.findById(transactionId);
		return transaction.map(t -> chargeHistoryMapper.mapFromEntity(t));
	}

	public ChargeHistoryEntity putHistoryData(final ChargeHistory chargeHistory) {
		return chargeHistoryRepository.save(chargeHistoryMapper.mapToEntity(chargeHistory));
	}

	public boolean hasAnEarlierRecordWithLaterEndTime(final Long vehicleId, final LocalDateTime startTime) {
		return chargeHistoryRepository.existsByVehicleIdAndEndTimeGreaterThan(vehicleId, startTime);
	}

	private List<ChargeHistoryEntity> findByVehicle(final Long vehicleId, final boolean sortStartDate,
			final boolean sortEndDate) {

		if (sortStartDate)
			return chargeHistoryRepository.findByVehicleIdOrderByStartTimeAsc(vehicleId);
		else if (sortEndDate)
			return chargeHistoryRepository.findByVehicleIdOrderByEndTimeAsc(vehicleId);

		return chargeHistoryRepository.findByVehicleId(vehicleId);
	}

}
