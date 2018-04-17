package app.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlockerService {

	@Autowired
	private BlockedAddressRepository repository;

	@Transactional
	public void analyzeLogAndBlockAddresses(LocalDateTime startTime, TimeUnit timeUnit, int threshold) {
		LocalDateTime endTime = LocalDateTime.from(startTime).plusHours(timeUnit.toHours(1)).minusSeconds(1);

		repository.blockIpsInLog(startTime, endTime, threshold);
	}

	public List<BlockedAddress> getBlockedAddresses() {
		return repository.findAll();
	}

}
