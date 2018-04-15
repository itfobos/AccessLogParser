package app.cli;

import org.apache.commons.cli.ParseException;

enum Duration {
	DAILY("daily"),
	HOURLY("hourly");

	private final String value;

	Duration(String value) {
		this.value = value;
	}

	public static Duration parseDuration(String value) throws ParseException {
		if (HOURLY.value.equalsIgnoreCase(value)) {
			return HOURLY;
		} else if (DAILY.value.equals(value)) {
			return DAILY;
		} else {
			throw new ParseException("Illegal duration value: " + value);
		}
	}
}
