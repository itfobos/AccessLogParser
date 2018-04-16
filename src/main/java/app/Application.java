package app;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import app.cli.CliParser;
import app.parsing.FileParser;
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
		basePackageClasses = { Application.class, Jsr310JpaConverters.class }
)
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		CliParser cliParser = CliParser.fromCliArgs(args);
		if (cliParser.argumentsAreCorrect()) {
			SpringApplication app = new SpringApplication(Application.class);
			app.setBannerMode(Banner.Mode.OFF);
			app.run(args);
		} else {
			cliParser.printErrors();
			CliParser.printUsage();
		}
	}

	@Bean
	CommandLineRunner runner(FileParser fileParser) {
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

			logger.info("Execution took: {} sec.", executionTime);
		};
	}
}
