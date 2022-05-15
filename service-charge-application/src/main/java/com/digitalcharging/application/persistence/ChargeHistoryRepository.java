package com.digitalcharging.application.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeHistoryRepository extends CrudRepository<ChargeHistoryEntity, Long> {

	List<ChargeHistoryEntity> findByVehicleId(Long vehicleId);

	List<ChargeHistoryEntity> findByVehicleIdOrderByStartTimeAsc(Long vehicleId);

	List<ChargeHistoryEntity> findByVehicleIdOrderByEndTimeAsc(Long vehicleId);

	Optional<ChargeHistoryEntity> findById(Long transactionId);

	public boolean existsByVehicleIdAndEndTimeGreaterThan(Long vehicleId, LocalDateTime startTime);

}
