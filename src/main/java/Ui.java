import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Builds the text responses shown to the user in the chat GUI.
 */
public class Ui {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /**
     * Returns the startup greeting.
     */
    public String showWelcome() {
        return "Hello! I'm Duke.\nWhat can I do for you?";
    }

    /**
     * Returns the goodbye message.
     */
    public String showGoodbye() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns a loading error and a fallback note.
     *
     * @param reason low-level reason for the loading failure
     */
    public String showLoadingError(String reason) {
        return "Warning: couldn't load saved tasks (" + reason + ").\nStarting with an empty task list.";
    }

    /**
     * Returns a standard task-added response.
     *
     * @param task added task
     * @param taskCount new number of tasks in the list
     */
    public String showTaskAdded(Task task, int taskCount) {
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /**
     * Returns all tasks in list order.
     *
     * @param taskList task list to display
     */
    public String showTasks(TaskList taskList) {
        List<Task> tasks = taskList.getAll();
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            response.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return response.toString();
    }

    /**
     * Returns tasks that occur on a date.
     *
     * @param targetDate date used for filtering
     * @param matchingTasks tasks on the date
     */
    public String showTasksOnDate(LocalDate targetDate, List<Task> matchingTasks) {
        String formattedDate = targetDate.format(DISPLAY_DATE_FORMAT);
        if (matchingTasks.isEmpty()) {
            return "No tasks are scheduled for " + formattedDate + ".";
        }

        StringBuilder response = new StringBuilder("Here are the tasks on " + formattedDate + ":");
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append("\n").append(i + 1).append(".").append(matchingTasks.get(i));
        }
        return response.toString();
    }

    /**
     * Returns tasks whose description matches a find keyword.
     *
     * @param matchingTasks tasks matching the keyword
     */
    public String showMatchingTasks(List<Task> matchingTasks) {
        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append("\n").append(i + 1).append(".").append(matchingTasks.get(i));
        }
        return response.toString();
    }

    /**
     * Returns a response reporting that a task was marked complete.
     *
     * @param task marked task
     */
    public String showTaskMarkedDone(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Returns a response reporting that a task was marked incomplete.
     *
     * @param task unmarked task
     */
    public String showTaskMarkedNotDone(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Returns a response reporting that a task was deleted.
     *
     * @param removedTask deleted task
     * @param remainingTaskCount task count after deletion
     */
    public String showTaskDeleted(Task removedTask, int remainingTaskCount) {
        return "Noted. I've removed this task:\n  " + removedTask
                + "\nNow you have " + remainingTaskCount + " tasks in the list.";
    }
}
