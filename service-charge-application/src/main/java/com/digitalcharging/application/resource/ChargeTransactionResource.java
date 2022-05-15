package com.digitalcharging.application.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalcharging.application.service.ChargeHistoryService;
import com.digitalcharging.application.service.dto.ChargeHistory;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Validated
@RequestMapping("/history")
@OpenAPIDefinition(info = @Info(title = "Charge History API", description = "Charge History API", contact = @Contact(name = "Gizem", email = "gizemyilmaz1988@gmail.com")))
public class ChargeTransactionResource {
	public static final class Constants {
		public static final String CHARGES_WITH_VEHICLE = "/charges/vehicles/{id}";
		public static final String CHARGES_WITH_ID = "/charges/{id}";
		public static final String CHARGES = "/charges";
	}

	@Autowired
	private ChargeHistoryService chargeHistoryService;

	@Operation(summary = "List history data of spesific vehicle", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved") })
	@GetMapping(Constants.CHARGES_WITH_VEHICLE)
	@Cacheable(value = "transactionsPerVehicle", key = "T(String).valueOf(#vehicleId) + T(String).valueOf(#sortStartDate) + T(String).valueOf(#sortEndDate)")
	public ResponseEntity<List<ChargeHistory>> getHistoryForVehicle(@PathVariable("id") Long vehicleId,
			@RequestParam(defaultValue = "false") boolean sortStartDate,
			@RequestParam(defaultValue = "false") boolean sortEndDate) {
		return ResponseEntity.ok()
				.body(chargeHistoryService.getHistoryForVehicle(vehicleId, sortStartDate, sortEndDate));

	}

	@Operation(summary = "Get spesific transaction", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved") })
	@GetMapping(Constants.CHARGES_WITH_ID)
	@Cacheable(value = "transactionPerId", key = "T(String).valueOf(#transactionId)")
	public ResponseEntity<Optional<ChargeHistory>> getHistory(@PathVariable("id") Long transactionId) {
		return ResponseEntity.ok().body(chargeHistoryService.getHistoryWithId(transactionId));
	}

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Add a  transaction", responses = {
			@ApiResponse(responseCode = "201", description = "Successfully added") })
	@PutMapping(Constants.CHARGES)
	public ResponseEntity addTransaction(@Valid @RequestBody ChargeHistory chargeHistory) {
		boolean hasAnEarlierRecordWithLaterEndTime = chargeHistoryService.hasAnEarlierRecordWithLaterEndTime(
				chargeHistory.getVehicleId(), chargeHistory.getTransactionRange().getStartTime());

		if (hasAnEarlierRecordWithLaterEndTime) {
			return ResponseEntity.badRequest().build();
		}

		chargeHistoryService.putHistoryData(chargeHistory);
		return ResponseEntity.accepted().build();

	}
}
