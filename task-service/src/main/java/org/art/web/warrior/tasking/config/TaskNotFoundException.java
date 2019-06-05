package org.art.web.warrior.tasking.config;

public class TaskNotFoundException extends RuntimeException {

    private String nameId;

    public TaskNotFoundException(String message, String nameId) {
        super(message);
        this.nameId = nameId;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Task name ID: " + nameId;
    }
}
