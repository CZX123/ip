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

    /**
     * Constructs CodyApp class.
     */
    public CodyApp() {}

    @Override
    public void start(Stage stage) {
        Ui.getInstance().start(this, stage);

        TaskList tasks;
        try {
            tasks = Storage.getInstance().load();
        } catch (StorageOperationException e) {
            Ui.getInstance().showNonFatalError(e.getMessage());
            tasks = new TaskList();
        }
        this.tasks = tasks;

        Ui.getInstance().showWelcome();
    }

    /**
     * Responds to the given user command.
     *
     * @param fullCommand the full command string the user typed out
     */
    public void respond(String fullCommand) {
        Ui.getInstance().showUserCommand(fullCommand);
        try {
            Command c = Parser.parse(fullCommand);
            c.execute(tasks);
            if (c.isExit()) {
                Ui.getInstance().showGoodbye();
                // slight delay before closing so goodbye message can be read
                CompletableFuture.delayedExecutor(800, TimeUnit.MILLISECONDS)
                        .execute(Ui.getInstance()::close);
            }
        } catch (CodyException e) {
            Ui.getInstance().showCodyResponse(e.getMessage());
        }
    }
}
