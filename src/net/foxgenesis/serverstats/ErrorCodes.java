package net.foxgenesis.serverstats;
public final class ErrorCodes {
	public static final int FORMAT = 0;
	public static final int INVALID_URL = 1;
	public static final int INVALID_LANG = 2;

	public static final void error(final int error) {
		Logger.error(ExternalStrings.get("error")
				+ error);
	}
	
	public static final String errorText(final int error) {
		return ExternalStrings.get("error")
		+ error;
	}
}
