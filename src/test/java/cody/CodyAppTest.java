package cody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import cody.testutil.StorageStub;
import cody.testutil.UiStub;

class CodyAppTest {

    @Test
    void testStart() {
        CodyApp app = new CodyApp();
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();

        app.start(null, ui, storage);
        assertEquals(1, ui.getStartCallCount(), "UI start should be called once");
        assertEquals(1, ui.getShowWelcomeCallCount(), "UI should show welcome message once");
        assertEquals(1, storage.getLoadCallCount(), "Storage load should be called once");
    }

    @Test
    void testRespond() {
        CodyApp app = new CodyApp();
        UiStub ui = new UiStub();
        StorageStub storage = new StorageStub();
        app.start(null, ui, storage);

        String fullCommand = "random command";
        app.respond(fullCommand);

        assertEquals(fullCommand, ui.getUserCommands(), "UI should record the user command");
        assertFalse(ui.getCodyResponses().isEmpty(), "UI should show a response from Cody");
    }
}
