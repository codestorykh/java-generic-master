package com.codestorykh.generics.bounded;

/**
 * A generic class that can only accept Number types and their subclasses.
 * Demonstrates bounded type parameters.
 * @param <T> the type of number to be stored, must extend Number
 */
public class NumberBox<T extends Number> {
    private T number;

    public NumberBox(T number) {
        this.number = number;
    }

    public T getNumber() {
        return number;
    }

    public void setNumber(T number) {
        this.number = number;
    }

    public double sqrt() {
        return Math.sqrt(number.doubleValue());
    }

    public boolean isGreaterThan(NumberBox<? extends Number> other) {
        return this.number.doubleValue() > other.getNumber().doubleValue();
    }

    public boolean isLessThan(NumberBox<? extends Number> other) {
        return this.number.doubleValue() < other.getNumber().doubleValue();
    }

    public double add(NumberBox<? extends Number> other) {
        return this.number.doubleValue() + other.getNumber().doubleValue();
    }

    public double subtract(NumberBox<? extends Number> other) {
        return this.number.doubleValue() - other.getNumber().doubleValue();
    }

    public double multiply(NumberBox<? extends Number> other) {
        return this.number.doubleValue() * other.getNumber().doubleValue();
    }

    public double divide(NumberBox<? extends Number> other) {
        double divisor = other.getNumber().doubleValue();
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return this.number.doubleValue() / divisor;
    }

    @Override
    public String toString() {
        return number.toString();
    }
} 