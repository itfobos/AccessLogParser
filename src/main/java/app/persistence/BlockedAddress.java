package app.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BLOCKED_ADDRESSES")
public class BlockedAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(name = "IP_ADDRESS", nullable = false)
	public String ipAddress;

	@Column(name = "BLOCK_REASON", nullable = false)
	public String blockReason;
}
