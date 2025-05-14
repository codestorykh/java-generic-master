package com.codestorykh.taskmanager.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Generic Task class that can process input of type I and produce output of type O.
 * This class demonstrates several key generic concepts:
 * 1. Multiple type parameters (I for input, O for output)
 * 2. Generic functional interfaces (Function<I,O>)
 * 3. Type-safe processing
 * 
 * @param <I> The input type that this task will process
 * @param <O> The output type that this task will produce
 */
public class Task<I, O> {
    private final String taskId;
    private final String name;
    private final TaskPriority priority;
    private final I input;
    private final Function<I, O> processor;
    private TaskStatus status;
    private O result;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorMessage;

    /**
     * Creates a new task with the specified parameters.
     * 
     * @param name The name of the task
     * @param priority The priority level of the task
     * @param input The input data to be processed
     * @param processor The function that will process the input and produce output
     */
    public Task(String name, TaskPriority priority, I input, Function<I, O> processor) {
        this.taskId = UUID.randomUUID().toString();
        this.name = name;
        this.priority = priority;
        this.input = input;
        this.processor = processor;
        this.status = TaskStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Executes the task asynchronously and returns a CompletableFuture of the result.
     * This method demonstrates how generics can be used with concurrent processing.
     * 
     * @return CompletableFuture<O> containing the result of the task
     */
    public CompletableFuture<O> execute() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.status = TaskStatus.RUNNING;
                this.startedAt = LocalDateTime.now();
                
                // Process the input using the provided function
                O output = processor.apply(input);
                
                // Update task status and metadata
                this.result = output;
                this.status = TaskStatus.COMPLETED;
                this.completedAt = LocalDateTime.now();
                
                return output;
            } catch (Exception e) {
                // Handle any errors during processing
                this.status = TaskStatus.FAILED;
                this.errorMessage = e.getMessage();
                this.completedAt = LocalDateTime.now();
                throw new RuntimeException("Task execution failed: " + e.getMessage(), e);
            }
        });
    }

    // Getters
    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public I getInput() {
        return input;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public O getResult() {
        return result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the duration of task execution in milliseconds.
     * Returns -1 if the task hasn't completed or failed.
     */
    public long getDurationMillis() {
        if (startedAt != null && completedAt != null) {
            return java.time.Duration.between(startedAt, completedAt).toMillis();
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format("Task[id=%s, name=%s, status=%s, priority=%s]",
            taskId, name, status, priority);
    }
} 