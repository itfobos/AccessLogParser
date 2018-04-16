package app.parsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import app.persistence.CachingPersistService;
import app.persistence.LogItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileParser {
	private static final int DATE_INDEX = 0;
	private static final int IP_INDEX = 1;

	@Autowired
	private CachingPersistService persistService;

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public void parseAndPersistLog(File logFile) throws IOException {

		Stream<String> lines = Files.lines(Paths.get(logFile.toURI()));

		lines.map(line -> line.split("\\|"))
				.map(lineParts -> {
					LocalDateTime eventTime = LocalDateTime.parse(lineParts[DATE_INDEX], TIME_FORMATTER);

					return new LogItem(eventTime, lineParts[IP_INDEX]);
				})
				.forEach(persistService::persist);

		persistService.flushCache();
	}
}
