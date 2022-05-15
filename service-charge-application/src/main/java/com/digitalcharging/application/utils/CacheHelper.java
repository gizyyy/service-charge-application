package com.digitalcharging.application.utils;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;

public class CacheHelper {
	@CacheEvict(allEntries = true, value = { "transactionsPerVehicle" })
	@Scheduled(fixedDelay = 60000)
	public void clearCachePerVehicle() {
		System.out.println("Cache cleared");
	}

	@CacheEvict(allEntries = true, value = { "transactionsPerId" })
	@Scheduled(fixedDelay = 600000)
	public void clearCachePerId() {
		System.out.println("Cache cleared");
	}
}
