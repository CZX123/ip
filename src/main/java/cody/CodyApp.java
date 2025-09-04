package cody;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import cody.commands.base.Command;
import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.exceptions.StorageOperationException;
import cody.parser.Parser;
import cody.storage.Storage;
import cody.ui.Ui;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class of CodyApp.
 */
public class CodyApp extends Application {
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;
    private TaskList tasks;

    /**
     * Constructs CodyApp class.
     */
    public CodyApp() {
        storage = new Storage();
        ui = new Ui();
        parser = new Parser();
    }

    @Override
    public void start(Stage stage) {
        ui.start(this, stage);
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (StorageOperationException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }
        this.tasks = tasks;
        ui.showWelcome();
    }

    /**
     * Runs the given user command.
     *
     * @param fullCommand the full command string the user typed out
     */
    public void run(String fullCommand) {
        try {
            ui.showText(fullCommand);
            Command c = parser.parse(fullCommand);
            c.execute(tasks, ui, storage);
            if (c.isExit()) {
                ui.showGoodbye();
                CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS).execute(ui::close);
            }
        } catch (CodyException e) {
            ui.showError(e.getMessage());
        }
    }
}
