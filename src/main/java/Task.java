/**
 * Represents the information shared by every task type.
 */
public class Task {
    protected final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as complete.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the status character used in a task-list display.
     *
     * @return {@code X} when complete, otherwise a space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns whether this task has been marked as complete.
     *
     * @return {@code true} when complete, otherwise {@code false}
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns this task in the persistent storage format.
     *
     * @return storage line containing the type marker, status marker, and description
     */
    public String toStorageString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the common status and description portion of a task display.
     * Subclasses prepend their task-type marker and append any scheduling details.
     *
     * @return status marker followed by the task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
