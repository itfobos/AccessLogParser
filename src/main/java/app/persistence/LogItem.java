package app.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "LOG_ITEMS")
public class LogItem {

	protected LogItem() {
	}

	public LogItem(LocalDateTime accessed, String ipAddress) {
		this.accessed = accessed;
		this.ipAddress = ipAddress;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ACCESSED", nullable = false)
	private LocalDateTime accessed;

	@Column(name = "IP_ADDRESS", nullable = false)
	private String ipAddress;

	//TODO: Maybe we don't need getters/setters at all
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getAccessed() {
		return accessed;
	}

	public void setAccessed(LocalDateTime accessed) {
		this.accessed = accessed;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
