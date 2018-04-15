package app.persistence;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class CachingService {
	private List<LogItem> entityCache = new LinkedList<>();

	@Autowired
	private LogItemRepository itemRepository;

	@Value("${app.persistence.batch_size}")
	private int batchSize;

	public void persist(LogItem item) {
		entityCache.add(item);

		if (entityCache.size() >= batchSize) {
			flushCache();
		}
	}


	public void flushCache() {
		//We don't need make the method Transactional,
		//because CRUD methods of the Spring Data JPA repository implementation
		// are already annotated with @Transactional.
		//So 'saveAll' will be executed in one transaction
		itemRepository.saveAll(entityCache);
		entityCache.clear();
	}
}
