package com.codestorykh.generics.advanced;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utility class demonstrating advanced generic concepts and methods.
 */
public class GenericUtils {

    /**
     * Finds elements that match a predicate and transforms them.
     * Demonstrates bounded type parameters and function composition.
     */
    public static <T, R extends Comparable<R>> List<R> findAndTransform(
            List<T> items,
            Predicate<T> predicate,
            Function<T, R> transformer) {
        
        List<R> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(transformer.apply(item));
            }
        }
        return result;
    }

    /**
     * Merges two sorted lists while maintaining order.
     * Demonstrates upper bounded wildcards.
     */
    public static <T extends Comparable<? super T>> List<T> mergeSorted(
            List<? extends T> list1,
            List<? extends T> list2) {
        
        List<T> result = new ArrayList<>();
        Iterator<? extends T> it1 = list1.iterator();
        Iterator<? extends T> it2 = list2.iterator();
        
        T current1 = it1.hasNext() ? it1.next() : null;
        T current2 = it2.hasNext() ? it2.next() : null;
        
        while (current1 != null && current2 != null) {
            if (current1.compareTo(current2) <= 0) {
                result.add(current1);
                current1 = it1.hasNext() ? it1.next() : null;
            } else {
                result.add(current2);
                current2 = it2.hasNext() ? it2.next() : null;
            }
        }
        
        // Add remaining elements
        while (current1 != null) {
            result.add(current1);
            current1 = it1.hasNext() ? it1.next() : null;
        }
        while (current2 != null) {
            result.add(current2);
            current2 = it2.hasNext() ? it2.next() : null;
        }
        
        return result;
    }

    /**
     * Reverses a list in place.
     * Demonstrates lower bounded wildcards.
     */
    public static void reverse(List<?> list) {
        reverseHelper(list);
    }

    private static <T> void reverseHelper(List<T> list) {
        int size = list.size();
        for (int i = 0; i < size / 2; i++) {
            T temp = list.get(i);
            list.set(i, list.get(size - 1 - i));
            list.set(size - 1 - i, temp);
        }
    }

    /**
     * Finds the maximum element in a collection using multiple bounds.
     * Demonstrates multiple bounds with type parameters.
     */
    public static <T extends Comparable<T> & Cloneable> T findMax(Collection<T> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Collection is empty");
        }
        
        return Collections.max(items);
    }

    /**
     * Safely casts an object to a type, returning Optional.
     * Demonstrates type safety with generics.
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> safeCast(Object obj, Class<T> type) {
        return type.isInstance(obj) ? Optional.of((T) obj) : Optional.empty();
    }

    /**
     * Creates a type-safe heterogeneous container.
     * Demonstrates type tokens and heterogeneous containers.
     */
    public static class TypeSafeMap {
        private Map<Class<?>, Object> map = new HashMap<>();
        
        public <T> void put(Class<T> type, T value) {
            map.put(Objects.requireNonNull(type), value);
        }
        
        @SuppressWarnings("unchecked")
        public <T> T get(Class<T> type) {
            return type.cast(map.get(type));
        }
    }
} 