package com.codestorykh.advanced;

import com.codestorykh.generics.advanced.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for demonstrating advanced generic concepts with Pair
 */
public class PairTest {

    @Test
    void testBasicPairOperations() {
        // Given
        Pair<String, Integer> pair = new Pair<>("test", 42);
        
        // Then
        assertEquals("test", pair.getKey());
        assertEquals(Integer.valueOf(42), pair.getValue());
        assertEquals("(test, 42)", pair.toString());
    }

    @Test
    void testStaticFactoryMethod() {
        // When
        Pair<String, Double> pair = Pair.of("pi", 3.14);
        
        // Then
        assertEquals("pi", pair.getKey());
        assertEquals(3.14, pair.getValue(), 0.0001);
    }

    @Test
    void testSetValue() {
        // Given
        Pair<String, Integer> pair = new Pair<>("test", 42);
        
        // When
        pair.setValue(100);
        
        // Then
        assertEquals(Integer.valueOf(100), pair.getValue());
    }

    @Test
    void testMaxPairComparison() {
        // Given
        Pair<String, Integer> pair1 = new Pair<>("A", 1);
        Pair<String, Integer> pair2 = new Pair<>("B", 2);
        
        // When
        Pair<String, Integer> maxPair = Pair.max(pair1, pair2);
        
        // Then
        assertEquals(pair2, maxPair);
        assertEquals("B", maxPair.getKey());
        assertEquals(Integer.valueOf(2), maxPair.getValue());
    }

    @Test
    void testSameKey() {
        // Given
        Pair<String, Integer> pair1 = new Pair<>("test", 42);
        Pair<String, String> pair2 = new Pair<>("test", "value");
        Pair<String, Double> pair3 = new Pair<>("other", 3.14);
        
        // Then
        assertTrue(pair1.sameKey(pair2));
        assertFalse(pair1.sameKey(pair3));
    }

    @Test
    void testCopyValueTo() {
        // Given
        Pair<String, Integer> source = new Pair<>("source", 42);
        Pair<Object, Number> destination = new Pair<>(null, 0);
        
        // When
        source.copyValueTo(destination);
        
        // Then
        assertEquals(Integer.valueOf(42), destination.getValue());
    }

    @Test
    void testWithDifferentTypes() {
        // Given
        Pair<Integer, String> numericKey = new Pair<>(1, "one");
        Pair<Double, Boolean> doubleKey = new Pair<>(1.0, true);
        
        // Then
        assertEquals(Integer.valueOf(1), numericKey.getKey());
        assertEquals("one", numericKey.getValue());
        assertEquals(1.0, doubleKey.getKey(), 0.0001);
        assertTrue(doubleKey.getValue());
    }

    @Test
    void testMaxPairWithEqualKeys() {
        // Given
        Pair<String, Integer> pair1 = new Pair<>("A", 1);
        Pair<String, Integer> pair2 = new Pair<>("A", 2);
        
        // When
        Pair<String, Integer> maxPair = Pair.max(pair1, pair2);
        
        // Then
        assertEquals(pair1, maxPair); // Should return first pair when keys are equal
    }
} 