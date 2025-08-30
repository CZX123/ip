package cody;

import cody.commands.base.Command;
import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.exceptions.StorageOperationException;
import cody.parser.Parser;
import cody.storage.Storage;
import cody.ui.Ui;

/**
 * Main class of Cody.
 */
public class Cody {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    /**
     * Constructs Cody class.
     */
    public Cody() {
        storage = new Storage();
        ui = new Ui();
        parser = new Parser();
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (StorageOperationException e) {
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
                Command c = parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (CodyException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showGoodbye();
    }

    public static void main(String[] args) {
        new Cody().run();
    }
}
