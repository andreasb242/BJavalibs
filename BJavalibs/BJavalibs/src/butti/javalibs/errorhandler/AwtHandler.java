package butti.javalibs.errorhandler;

public class AwtHandler {
	public void handle(Throwable t) {
		Errorhandler.showError(t, "Event loop error");
	}
}