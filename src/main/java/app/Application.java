package app;

import app.cli.CliParser;
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
	CommandLineRunner runner() {
		return args -> {
			//TODO:
		};
	}
}
