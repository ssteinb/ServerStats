package net.foxgenesis.serverstats;

public final class ErrorCodes {
	public static final String error(int error) {
		return "An error has occured. Please contact your administrator. Ref = " + error;
	}
	public static final int FORMAT = 0x00;
	public static final int WEB_REQUEST = 0x01;
}
