package com.codestorykh.generics.basic;

/**
 * A basic generic class that can hold any type of object.
 * @param <T> the type of object to be stored
 */
public class Box<T> {
    private T content;

    public Box() {
    }

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean hasContent() {
        return content != null;
    }

    public void clear() {
        content = null;
    }

    @Override
    public String toString() {
        return content != null ? content.toString() : "empty box";
    }

    // Generic method example
    public <U> Box<U> transform(Transformer<T, U> transformer) {
        if (content == null) {
            return new Box<>();
        }
        return new Box<>(transformer.transform(content));
    }

    // Functional interface for transformation
    @FunctionalInterface
    public interface Transformer<T, U> {
        U transform(T input);
    }
} 