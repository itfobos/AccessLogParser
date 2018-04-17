package com.ef.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "LOG_ITEMS")
public class LogItem {

	protected LogItem() {
	}

	public LogItem(LocalDateTime accessed, String ipAddress) {
		this.accessed = accessed;
		this.ipAddress = ipAddress;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(name = "ACCESSED", nullable = false)
	public LocalDateTime accessed;

	@Column(name = "IP_ADDRESS", nullable = false)
	public String ipAddress;
}
