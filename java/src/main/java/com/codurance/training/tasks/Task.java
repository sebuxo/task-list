package com.codurance.training.tasks;

public final class Task {
    private final Integer id;
    private final String description;
    private Boolean taskDone;

    public Task(Integer id, String description, boolean done) {
        this.id = id;
        this.description = description;
        this.taskDone = done;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return taskDone;
    }

    public void setDone(boolean done) {
        this.taskDone = done;
    }
}
