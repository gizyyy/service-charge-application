package com.digitalcharging;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.digitalcharging.application.persistence.ChargeHistoryEntity;
import com.digitalcharging.application.resource.ChargeTransactionResource;
import com.digitalcharging.application.service.ChargeHistoryService;
import com.digitalcharging.application.service.dto.ChargeHistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@AllArgsConstructor
@Getter
@Setter
class ChargeHistoryAssertion {
	private Long transactionId;
	private Long vehicleId;
	private TranactionRange transactionRange;
	private BigDecimal cost;
	private boolean valid;
}

@AllArgsConstructor
@Getter
@Setter
class TranactionRange {
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}

class ChargeHistoryAssertionAccessor implements ArgumentsAggregator {

	@Override
	public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
			throws ArgumentsAggregationException {

		final java.util.List<String> tokens = Collections.list(new StringTokenizer(accessor.getString(0), "/")).stream()
				.map(token -> (String) token).collect(Collectors.toList());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

		return new ChargeHistoryAssertion(null, Long.valueOf(tokens.get(0)),
				new TranactionRange(LocalDateTime.parse(tokens.get(1), formatter),
						LocalDateTime.parse(tokens.get(2), formatter)),
				new BigDecimal(tokens.get(3)), accessor.getBoolean(1));
	}
}

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = { ChargeTransactionResource.class, ChargeHistoryService.class })
public class ChargeHistoryIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	ChargeHistoryService chargeHistoryService;

	@BeforeTestClass
	public void setup() {
		this.mvc = MockMvcBuilders.standaloneSetup(new ChargeTransactionResource()).build();
	}

	@SneakyThrows
	@ParameterizedTest
	@CsvFileSource(resources = { "/data.csv" })
	@DisplayName("Should be able to put valid data")
	public void putVehicle(@AggregateWith(ChargeHistoryAssertionAccessor.class) ChargeHistoryAssertion history) {
		when(chargeHistoryService.getHistoryForVehicle(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean()))
				.thenReturn(null);
		when(chargeHistoryService.putHistoryData(Mockito.any(ChargeHistory.class)))
				.thenReturn(new ChargeHistoryEntity());

		ResultActions perform = mvc
				.perform(MockMvcRequestBuilders.put("/history/charges").content(asJsonString(history))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		if (history.isValid()) {
			perform.andExpect(status().isAccepted());
		} else {
			perform.andExpect(status().isBadRequest());
		}
	}

	private static String asJsonString(final Object obj) {
		try {
			ObjectMapper m = new ObjectMapper();
			m.registerModule(new JSR310Module());
			m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			return m.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
