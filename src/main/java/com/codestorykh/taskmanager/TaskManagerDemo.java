package com.codestorykh.taskmanager;

import com.codestorykh.taskmanager.model.Task;
import com.codestorykh.taskmanager.model.TaskPriority;
import com.codestorykh.taskmanager.service.TaskManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TaskManagerDemo {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        try {
            // Example 1: String processing task
            System.out.println("Creating string processing task...");
            Task<String, Integer> wordCountTask = taskManager.submitTask(
                "Word Count",
                TaskPriority.HIGH,
                "Hello World! Welcome to Task Management",
                input -> input.split("\\s+").length
            );

            // Example 2: Number processing task
            System.out.println("Creating number processing task...");
            Task<List<Integer>, Double> averageTask = taskManager.submitTask(
                "Calculate Average",
                TaskPriority.MEDIUM,
                Arrays.asList(1, 2, 3, 4, 5),
                numbers -> numbers.stream()
                    .mapToDouble(Integer::doubleValue)
                    .average()
                    .orElse(0.0)
            );

            // Example 3: Object transformation task
            System.out.println("Creating object transformation task...");
            Task<String, Person> personCreationTask = taskManager.submitTask(
                "Create Person",
                TaskPriority.LOW,
                "John Doe,30",
                input -> {
                    String[] parts = input.split(",");
                    return new Person(parts[0], Integer.parseInt(parts[1]));
                }
            );

            // Execute tasks and get results
            CompletableFuture<Integer> wordCountFuture = taskManager.executeTask(wordCountTask);
            CompletableFuture<Double> averageFuture = taskManager.executeTask(averageTask);
            CompletableFuture<Person> personFuture = taskManager.executeTask(personCreationTask);

            // Wait for and print results
            System.out.println("\nTask Results:");
            System.out.println("Word Count: " + wordCountFuture.get());
            System.out.println("Average: " + averageFuture.get());
            System.out.println("Person: " + personFuture.get());

            // Example 4: Batch task execution
            System.out.println("\nCreating batch of number processing tasks...");
            List<Task<Integer, Integer>> numberTasks = Arrays.asList(
                taskManager.submitTask("Square 1", TaskPriority.LOW, 5, n -> n * n),
                taskManager.submitTask("Square 2", TaskPriority.LOW, 6, n -> n * n),
                taskManager.submitTask("Square 3", TaskPriority.LOW, 7, n -> n * n)
            );

            List<CompletableFuture<Integer>> batchResults = taskManager.executeTasksBatch(numberTasks);
            System.out.println("Batch Results:");
            for (int i = 0; i < batchResults.size(); i++) {
                System.out.printf("Square of %d: %d%n", i + 5, batchResults.get(i).get());
            }

            // Print task statistics
            System.out.println("\nTask Statistics:");
            taskManager.getTaskStatistics().forEach((status, count) ->
                System.out.printf("%s: %d tasks%n", status, count));

            // Example 5: Error handling
            System.out.println("\nTesting error handling...");
            Task<String, Integer> errorTask = taskManager.submitTask(
                "Error Task",
                TaskPriority.HIGH,
                "invalid input",
                input -> {
                    throw new RuntimeException("Simulated error");
                }
            );

            try {
                taskManager.executeTask(errorTask).get();
            } catch (ExecutionException e) {
                System.out.println("Task failed as expected: " + e.getCause().getMessage());
            }

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    // Simple Person class for demonstration
    private static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("Person[name=%s, age=%d]", name, age);
        }
    }
} 