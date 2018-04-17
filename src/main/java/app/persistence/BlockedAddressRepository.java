package app.persistence;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/*package*/ interface BlockedAddressRepository extends JpaRepository<BlockedAddress, Long> {

	@Modifying
	@Query("INSERT INTO BlockedAddress(ipAddress, blockReason) "
			+ "SELECT log.ipAddress, CONCAT('Exceeded threshold(',:threshold,').  Requests amount: ', count(*)) AS blockReason FROM LogItem log "
			+ "WHERE log.accessed BETWEEN :startTime AND :endTime "
			+ "GROUP BY log.ipAddress "
			+ "HAVING COUNT(*)>:threshold")
	void blockIpsInLog(@Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime,
			@Param("threshold") long threshold);
}
