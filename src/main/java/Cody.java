public class Cody {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Cody() {
        storage = new Storage();
        ui = new Ui();
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (Storage.StorageOperationException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }
        this.tasks = tasks;
    }

    private void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command command = Command.of(fullCommand);
                switch (command) {
                case BYE, EXIT -> isExit = true;
                case LIST -> listTasks(fullCommand);
                case TODO, DEADLINE, EVENT -> addTask(command, fullCommand);
                case MARK, UNMARK -> markTask(command, fullCommand);
                case DELETE -> deleteTask(fullCommand);
                default -> throw new CodyException("⚠\uFE0F Invalid command!");
                }
            } catch (CodyException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showGoodbye();
    }

    public static void main(String[] args) {
        new Cody().run();
    }

    private void listTasks(String inputTxt) {
        String result;
        if (tasks.isEmpty()) {
            result = "You have no tasks saved! \uD83D\uDE0E";
        } else {
            result = String.format("You have %d task%s! \uD83D\uDCAA\uD83D\uDCDD\n%s",
                    tasks.size(), tasks.isSingular() ? "" : "s", tasks);
        }
        ui.showCommandResult(result);
    }

    private void addTask(Command command, String inputTxt) throws CodyException {
        Task task;
        switch (command) {
            case TODO -> task = Todo.fromCommand(inputTxt);
            case DEADLINE -> task = Deadline.fromCommand(inputTxt);
            case EVENT -> task = Event.fromCommand(inputTxt);
            default -> throw new Error("addTask called incorrectly!"); // should never happen
        }
        tasks.add(task);
        String result = String.format("➕ Added task:\n   %s\n\n\uD83D\uDCCB Now there %s %d task%s!",
                task, tasks.isSingular() ? "is" : "are", tasks.size(), tasks.isSingular() ? "" : "s");
        ui.showCommandResult(result);
        storage.save(tasks);
    }

    private void markTask(Command command, String inputTxt) throws CodyException {
        int index = getTaskIndex(inputTxt);
        String result;
        if (command == Command.MARK) {
            tasks.get(index).markDone();
            result = "✅ Marked task as done:\n   " + tasks.get(index);
        } else {
            tasks.get(index).unmarkDone();
            result = "↩\uFE0F Marked task as not done:\n   " + tasks.get(index);
        }
        ui.showCommandResult(result);
        storage.save(tasks);
    }

    private void deleteTask(String inputTxt) throws CodyException {
        int index = getTaskIndex(inputTxt);
        Task task = tasks.get(index);
        tasks.remove(index);
        String result = String.format("\uD83D\uDDD1️ Removed task:\n   %s\n\n\uD83D\uDCCB Now there %s %d task%s!",
                task, tasks.isSingular() ? "is" : "are", tasks.size(), tasks.isSingular() ? "" : "s");
        ui.showCommandResult(result);
        storage.save(tasks);
    }

    private int getTaskIndex(String inputTxt) throws CodyException {
        int index;
        try {
            index = Integer.parseInt(inputTxt.split(" ", 2)[1]) - 1;
        } catch (Exception e) {
            throw new CodyException("Please enter a valid task number! \uD83E\uDD74\n"
                    + "To view task number, type \"list\".");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new CodyException(String.format("There is no task numbered %d! \uD83D\uDE35", index + 1));
        }
        return index;
    }
}
