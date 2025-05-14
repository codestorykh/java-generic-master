package com.codestorykh.taskmanager.service;

import com.codestorykh.taskmanager.model.Task;
import com.codestorykh.taskmanager.model.TaskPriority;
import com.codestorykh.taskmanager.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskManager {
    private final Map<String, Task<?, ?>> tasks;

    public TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
    }

    // Create and submit a new task
    public <I, O> Task<I, O> submitTask(String name, TaskPriority priority, I input, Function<I, O> processor) {
        Task<I, O> task = new Task<>(name, priority, input, processor);
        tasks.put(task.getTaskId(), task);
        return task;
    }

    // Execute a task and get its future result
    public <I, O> CompletableFuture<O> executeTask(Task<I, O> task) {
        return task.execute();
    }

    // Get task by ID with type safety
    @SuppressWarnings("unchecked")
    public <I, O> Task<I, O> getTask(String taskId) {
        return (Task<I, O>) tasks.get(taskId);
    }

    // Get all tasks of a specific status
    public List<Task<?, ?>> getTasksByStatus(TaskStatus status) {
        return tasks.values().stream()
            .filter(task -> task.getStatus() == status)
            .collect(Collectors.toList());
    }

    // Get all tasks of a specific priority
    public List<Task<?, ?>> getTasksByPriority(TaskPriority priority) {
        return tasks.values().stream()
            .filter(task -> task.getPriority() == priority)
            .collect(Collectors.toList());
    }

    // Execute multiple tasks in parallel
    public <I, O> List<CompletableFuture<O>> executeTasksBatch(List<Task<I, O>> taskList) {
        return taskList.stream()
            .map(this::executeTask)
            .collect(Collectors.toList());
    }

    // Get all tasks
    public List<Task<?, ?>> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Clear completed tasks
    public void clearCompletedTasks() {
        tasks.entrySet().removeIf(entry -> 
            entry.getValue().getStatus() == TaskStatus.COMPLETED);
    }

    // Get task statistics
    public Map<TaskStatus, Long> getTaskStatistics() {
        return tasks.values().stream()
            .collect(Collectors.groupingBy(
                Task::getStatus,
                Collectors.counting()
            ));
    }
} 