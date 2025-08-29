package data;

import java.time.LocalDate;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public char getLetter() {
        return 'T';
    }

    @Override
    public boolean fallsOn(LocalDate date) {
        return false;
    }
}
