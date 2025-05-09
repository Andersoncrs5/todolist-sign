package br.com.todolist.api.DTO;

import br.com.todolist.api.Models.TaskModel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskDto{
    private Long id;

    @NotBlank
    @Max(50)
    @Min(5)
    private String title;

    @Max(100)
    @NotBlank
    private String description;

    private boolean done;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    private Long userId;



    public TaskModel mappearToTaskModel() {
        TaskModel task = new TaskModel();

        task.setId(getId());
        task.setTitle(getTitle());
        task.setDescription(getTitle());
        task.setDone(done);
        task.setUserId(userId);

        return task;
    }
}
