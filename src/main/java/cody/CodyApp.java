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
     * Runs the given user command.
     *
     * @param fullCommand the full command string the user typed out
     */
    public void run(String fullCommand) {
        try {
            Ui.getInstance().showUserCommand(fullCommand);
            Command c = Parser.parse(fullCommand);
            c.execute(tasks);
            if (c.isExit()) {
                Ui.getInstance().showGoodbye();
                CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS).execute(Ui.getInstance()::close);
            }
        } catch (CodyException e) {
            Ui.getInstance().showCodyResponse(e.getMessage());
        }
    }
}
