package matchingengine.app;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String msg) {
        super(msg);
    }
}
