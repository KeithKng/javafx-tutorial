/**
 * Represents an invalid command entered into the Duke chatbot.
 */
public class DukeException extends Exception {
    private final String suggestion;

    /**
     * Creates an exception with an explanation and a way to correct the command.
     */
    public DukeException(String message, String suggestion) {
        super(message);
        this.suggestion = suggestion;
    }

    /**
     * Returns the complete user-facing error response.
     */
    public String getUserMessage() {
        return "Error: " + getMessage() + System.lineSeparator() + "Try: " + suggestion;
    }
}
