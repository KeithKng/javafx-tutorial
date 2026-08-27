import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

/**
 * Represents a task that takes place between a specified start and end time.
 */
public class Event extends Task {
    private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description text describing the event
     * @param from date or time at which the event starts
     * @param to date or time at which the event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Checks whether this event occurs on the given date.
     *
     * @param targetDate date to check
     * @return {@code true} when the target date falls within the event interval
     */
    public boolean occursOn(LocalDate targetDate) {
        LocalDateTime start = parseDateTime(from);
        LocalDateTime end = parseDateTime(to);
        if (start == null || end == null) {
            return false;
        }
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();
        return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    /**
     * Returns this event in the command-line display format.
     *
     * @return event type marker, status marker, description, start text, and end text
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns this event in the persistent storage format.
     *
     * @return storage line containing task type, status, description, start text, and end text
     */
    @Override
    public String toStorageString() {
        return "E | " + (isDone() ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    /**
     * Attempts to parse the given text as a date or date-time in one of the supported formats.
     *
     * @param text raw start or end text
     * @return parsed date-time, or {@code null} when the text does not match any supported format
     */
    private static LocalDateTime parseDateTime(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
                // fall through to next format
            }
        }

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(trimmed, formatter);
                return date.atStartOfDay();
            } catch (DateTimeParseException ignored) {
                // fall through to next format
            }
        }
        return null;
    }
}
