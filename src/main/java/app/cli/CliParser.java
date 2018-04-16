package app.cli;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliParser {
	private final List<String> errors = new ArrayList<>(OPTIONS.getRequiredOptions().size());

	private Date startDate;
	private TimeUnit duration;
	private int threshold;
	private File logFile;

	private CliParser() {
	}

	public static CliParser fromCliArgs(String... args) {
		CliParser controller = new CliParser();

		controller.parseArgs(args);

		return controller;
	}

	private void parseArgs(String... args) {
		CommandLine commandLine;
		try {
			commandLine = PARSER.parse(OPTIONS, args);
		} catch (ParseException e) {
			this.errors.add(e.getMessage());
			return;
		}

		try {
			startDate = DATE_FORMAT.parse(commandLine.getOptionValue(START_DATE_OPT));
		} catch (java.text.ParseException e) {
			this.errors.add(e.getMessage());
		}

		try {
			Duration parsedDuration = Duration.parseDuration(commandLine.getOptionValue(DURATION_OPT));
			if (parsedDuration == Duration.HOURLY) {
				this.duration = TimeUnit.HOURS;
			} else {
				this.duration = TimeUnit.DAYS;
			}
		} catch (ParseException e) {
			this.errors.add(e.getMessage());
		}

		try {
			threshold = Integer.parseInt(commandLine.getOptionValue(THRESHOLD_OPT));
			if (threshold <= 0) {
				errors.add(THRESHOLD_OPT + " should be positive");
			}
		} catch (NumberFormatException e) {
			this.errors.add(e.getMessage());
		}

		String logFilePath = commandLine.getOptionValue(ACCESSLOG_OPT);
		logFile = new File(logFilePath);
		if (!logFile.exists()) {
			errors.add("Defined log file '" + logFilePath + "' does not exist.");
		} else if (logFile.isDirectory()) {
			errors.add("Defined path '" + logFilePath + "' is directory.");
		}

	}

	public boolean argumentsAreCorrect() {
		return errors.isEmpty();
	}

	/**
	 * Prints errors to {@link System#err}
	 */
	public void printErrors() {
		errors.forEach(System.err::println);
	}

	public static void printUsage() {
		HELP_FORMATTER.printHelp("parser", OPTIONS);
	}

	public Date getStartDate() {
		return startDate;
	}

	public TimeUnit getDuration() {
		return duration;
	}

	public int getThreshold() {
		return threshold;
	}

	public File getLogFile() {
		return logFile;
	}

	private static final CommandLineParser PARSER = new DefaultParser();

	private static final Options OPTIONS = new Options();
	private static final String START_DATE_OPT = "startDate";
	private static final String DURATION_OPT = "duration";
	private static final String THRESHOLD_OPT = "threshold";
	private static final String ACCESSLOG_OPT = "accesslog";

	private static final DateFormat DATE_FORMAT;
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd.HH:mm:ss";

	private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();

	static {
		DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

		OPTIONS.addOption(Option.builder().longOpt(START_DATE_OPT)
				.required()
				.hasArg()
				.argName("Date")
				.desc("Format: '" + DATE_FORMAT_PATTERN + "'")
				.build());

		OPTIONS.addOption(Option.builder().longOpt(DURATION_OPT)
				.required()
				.hasArg()
				.desc("Only 'hourly' or 'daily'")
				.build());

		OPTIONS.addOption(Option.builder().longOpt(THRESHOLD_OPT)
				.required()
				.hasArg()
				.desc("Positive integer value")
				.build());

		OPTIONS.addOption(Option.builder().longOpt(ACCESSLOG_OPT)
				.required()
				.hasArg()
				.desc("/path/to/file")
				.build());
	}


}
