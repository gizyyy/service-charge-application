package com.digitalcharging.application.service;

import org.springframework.stereotype.Service;

import com.digitalcharging.application.persistence.ChargeHistoryEntity;
import com.digitalcharging.application.service.dto.ChargeHistory;
import com.digitalcharging.application.service.dto.TransactionRange;

@Service
public class ChargeHistoryMapper {

	public ChargeHistory mapFromEntity(final ChargeHistoryEntity chargeHistoryEntity) {
		ChargeHistory chargeHistory = new ChargeHistory();
		chargeHistory.setTransactionRange(new TransactionRange());
		chargeHistory.getTransactionRange().setEndTime(chargeHistoryEntity.getEndTime());
		chargeHistory.getTransactionRange().setStartTime(chargeHistoryEntity.getStartTime());
		chargeHistory.setVehicleId(chargeHistoryEntity.getVehicleId());
		chargeHistory.setTransactionId(chargeHistoryEntity.getTransactionId());
		chargeHistory.setCost(chargeHistoryEntity.getCost());
		return chargeHistory;
	}

	public ChargeHistoryEntity mapToEntity(final ChargeHistory chargeHistory) {
		ChargeHistoryEntity chargeHistoryEntity = new ChargeHistoryEntity();
		chargeHistoryEntity.setEndTime(chargeHistory.getTransactionRange().getEndTime());
		chargeHistoryEntity.setStartTime(chargeHistory.getTransactionRange().getStartTime());
		chargeHistoryEntity.setVehicleId(chargeHistory.getVehicleId());
		chargeHistoryEntity.setCost(chargeHistory.getCost());
		return chargeHistoryEntity;
	}
}
