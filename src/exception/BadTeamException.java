package exception;

public class BadTeamException extends Exception {

	public BadTeamException() {
		super();
	}

	public BadTeamException(String mesg) {
		super(mesg);
	}
}
