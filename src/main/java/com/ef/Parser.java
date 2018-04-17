package com.ef;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.ef.cli.CliParser;
import com.ef.parsing.FileParser;
import com.ef.persistence.BlockedAddress;
import com.ef.persistence.BlockerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(
		basePackageClasses = { Parser.class, Jsr310JpaConverters.class }
)
public class Parser {

	private static final Logger logger = LoggerFactory.getLogger(Parser.class);

	public static void main(String[] args) {
		CliParser cliParser = CliParser.fromCliArgs(args);
		if (cliParser.argumentsAreCorrect()) {
			SpringApplication app = new SpringApplication(Parser.class);
			app.setBannerMode(Banner.Mode.OFF);
			app.run(args);
		} else {
			cliParser.printErrors();
			CliParser.printUsage();
		}
	}

	@Bean
	CommandLineRunner runner(FileParser fileParser, BlockerService blockerService) {
		return args -> {
			CliParser cliParser = CliParser.fromCliArgs(args);

			final long startTime = System.currentTimeMillis();
			try {
				fileParser.parseAndPersistLog(cliParser.getLogFile());
			} catch (IOException e) {
				System.err.println("Unable to parse '" +
						cliParser.getLogFile().getPath() + "' file: " +
						e.getMessage());
				throw e;
			}

			long executionTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);
			logger.info("Log import took: {} sec.", executionTime);

			blockerService.analyzeLogAndBlockAddresses(
					cliParser.getStartDate(),
					cliParser.getDuration(),
					cliParser.getThreshold()
			);

			List<BlockedAddress> blockedAddresses = blockerService.getBlockedAddresses();
			logger.info("Blocked addresses:\n {}",
					blockedAddresses.stream().map(item -> '\n' + item.ipAddress).collect(Collectors.toList()));

			logger.info("All execution took: {} sec.",
					TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime));
		};
	}
}
