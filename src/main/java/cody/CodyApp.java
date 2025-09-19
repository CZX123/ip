package cody;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.stage.Stage;

import cody.commands.base.Command;
import cody.data.TaskList;
import cody.exceptions.CodyException;
import cody.exceptions.StorageOperationException;
import cody.parser.Parser;
import cody.storage.Storage;
import cody.ui.Ui;

/**
 * Main class of CodyApp.
 */
public class CodyApp extends Application {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    @Override
    public void start(Stage stage) {
        start(stage, new Ui(), new Storage());
    }

    /**
     * Starts the application with the given UI and storage.
     *
     * @param stage   the primary stage for this application, supplied by {@code Application.start()}.
     * @param ui      the user interface to use.
     * @param storage the storage handler to use.
     */
    protected void start(Stage stage, Ui ui, Storage storage) {
        this.ui = ui;
        this.storage = storage;
        ui.start(this, stage);

        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (StorageOperationException e) {
            Ui.showNonFatalError(e.getMessage());
            tasks = new TaskList();
        }
        this.tasks = tasks;

        ui.showWelcome();
    }

    /**
     * Responds to the given user command.
     *
     * @param fullCommand the full command string the user typed out.
     */
    public void respond(String fullCommand) {
        ui.showUserCommand(fullCommand);
        try {
            Command c = Parser.parse(fullCommand);
            c.execute(tasks, ui, storage);
            if (c.isExit()) {
                ui.showGoodbye();
                // slight delay before closing so goodbye message can be read
                CompletableFuture.delayedExecutor(800, TimeUnit.MILLISECONDS)
                        .execute(Ui::close);
            }
        } catch (CodyException e) {
            ui.showCodyResponse(e.getMessage());
        }
    }
}
