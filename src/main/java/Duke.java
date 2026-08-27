import java.time.LocalDate;
import java.util.List;

public class Duke {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private String commandType;

    public Duke() {
        this("data/duke.txt");
    }

    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        List<Task> loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (DukeException e) {
            loadedTasks = List.of();
        }
        tasks = new TaskList(loadedTasks);
    }

    public static void main(String[] args) {
        System.out.println("Hello!");
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Parser.ParsedCommand parsedCommand = Parser.parse(input);
            commandType = parsedCommand.getCommand().name();
            return execute(parsedCommand);
        } catch (DukeException e) {
            commandType = null;
            return e.getUserMessage();
        }
    }

    public String getCommandType() {
        return commandType;
    }

    private String execute(Parser.ParsedCommand parsedCommand) throws DukeException {
        switch (parsedCommand.getCommand()) {
        case TODO:
            return addTodo(parsedCommand.getArguments());
        case DEADLINE:
            return addDeadline(parsedCommand.getArguments());
        case EVENT:
            return addEvent(parsedCommand.getArguments());
        case LIST:
            return ui.showTasks(tasks);
        case ONDATE:
            return showTasksOnDate(parsedCommand.getArguments());
        case MARK:
            return markTask(parsedCommand.getArguments());
        case UNMARK:
            return unmarkTask(parsedCommand.getArguments());
        case DELETE:
            return deleteTask(parsedCommand.getArguments());
        case FIND:
            return findTasks(parsedCommand.getArguments());
        case BYE:
            return ui.showGoodbye();
        default:
            throw new IllegalStateException("Unhandled command: " + parsedCommand.getCommand());
        }
    }

    private String addTodo(String arguments) throws DukeException {
        String description = Parser.parseTodoDescription(arguments);
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks);
        return ui.showTaskAdded(task, tasks.size());
    }

    private String addDeadline(String arguments) throws DukeException {
        Parser.DeadlineDetails details = Parser.parseDeadlineDetails(arguments);
        Task task = new Deadline(details.getDescription(), details.getBy());
        tasks.add(task);
        storage.save(tasks);
        return ui.showTaskAdded(task, tasks.size());
    }

    private String addEvent(String arguments) throws DukeException {
        Parser.EventDetails details = Parser.parseEventDetails(arguments);
        Task task = new Event(details.getDescription(), details.getFrom(), details.getTo());
        tasks.add(task);
        storage.save(tasks);
        return ui.showTaskAdded(task, tasks.size());
    }

    private String showTasksOnDate(String arguments) throws DukeException {
        LocalDate targetDate = Parser.parseOnDate(arguments);
        List<Task> matchingTasks = tasks.findTasksOnDate(targetDate);
        return ui.showTasksOnDate(targetDate, matchingTasks);
    }

    private String markTask(String arguments) throws DukeException {
        int taskNumber = Parser.parseTaskNumber(arguments, tasks.size(), "mark");
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        storage.save(tasks);
        return ui.showTaskMarkedDone(task);
    }

    private String unmarkTask(String arguments) throws DukeException {
        int taskNumber = Parser.parseTaskNumber(arguments, tasks.size(), "unmark");
        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        storage.save(tasks);
        return ui.showTaskMarkedNotDone(task);
    }

    private String deleteTask(String arguments) throws DukeException {
        int taskNumber = Parser.parseTaskNumber(arguments, tasks.size(), "delete");
        Task removedTask = tasks.remove(taskNumber - 1);
        storage.save(tasks);
        return ui.showTaskDeleted(removedTask, tasks.size());
    }

    private String findTasks(String arguments) throws DukeException {
        String keyword = Parser.parseFindKeyword(arguments);
        List<Task> matchingTasks = tasks.find(keyword);
        return ui.showMatchingTasks(matchingTasks);
    }
}
