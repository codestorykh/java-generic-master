package com.codestorykh.generics.advanced;

/**
 * A generic class that holds a pair of values of different types.
 * Demonstrates multiple type parameters.
 * @param <K> the type of the first value
 * @param <V> the type of the second value
 */
public class Pair<K, V> {
    private final K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }

    // Generic static factory method
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    // Demonstrates bounded type parameters with multiple bounds
    public static <K extends Comparable<K>, V extends Number> 
            Pair<K, V> max(Pair<K, V> p1, Pair<K, V> p2) {
        if (p1.getKey().compareTo(p2.getKey()) >= 0) {
            return p1;
        }
        return p2;
    }

    // Demonstrates wildcard usage
    public boolean sameKey(Pair<?, ?> other) {
        return key.equals(other.key);
    }

    // Demonstrates lower bounded wildcard
    public void copyValueTo(Pair<? super K, ? super V> dest) {
        dest.setValue(value);
    }
} 