package com.codestorykh.advanced;

import com.codestorykh.generics.advanced.GenericUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for demonstrating advanced generic utility methods
 */
public class GenericUtilsTest {

    @Test
    void testFindAndTransform() {
        // Given
        List<String> words = Arrays.asList("hello", "world", "of", "generics");
        
        // When
        List<Integer> lengths = GenericUtils.findAndTransform(
            words,
            word -> word.length() > 3,
            String::length
        );
        
        // Then
        assertEquals(Arrays.asList(5, 5, 8), lengths);
    }

    @Test
    void testMergeSorted() {
        // Given
        List<Integer> list1 = Arrays.asList(1, 3, 5);
        List<Integer> list2 = Arrays.asList(2, 4, 6);
        
        // When
        List<Integer> merged = GenericUtils.mergeSorted(list1, list2);
        
        // Then
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), merged);
    }

    @Test
    void testMergeSortedWithEmptyLists() {
        // Given
        List<Integer> emptyList = Arrays.asList();
        List<Integer> nonEmptyList = Arrays.asList(1, 2, 3);
        
        // When
        List<Integer> mergedEmpty = GenericUtils.mergeSorted(emptyList, emptyList);
        List<Integer> mergedWithEmpty = GenericUtils.mergeSorted(emptyList, nonEmptyList);
        
        // Then
        assertTrue(mergedEmpty.isEmpty());
        assertEquals(nonEmptyList, mergedWithEmpty);
    }

    @Test
    void testReverse() {
        // Given
        List<String> list = Arrays.asList("a", "b", "c", "d");
        
        // When
        GenericUtils.reverse(list);
        
        // Then
        assertEquals(Arrays.asList("d", "c", "b", "a"), list);
    }

    @Test
    void testReverseWithSingleElement() {
        // Given
        List<Integer> singleElement = Arrays.asList(1);
        
        // When
        GenericUtils.reverse(singleElement);
        
        // Then
        assertEquals(Arrays.asList(1), singleElement);
    }

    @Test
    void testSafeCast() {
        // Given
        Object stringObj = "Hello";
        Object intObj = 42;
        
        // When
        Optional<String> stringResult = GenericUtils.safeCast(stringObj, String.class);
        Optional<Integer> intResult = GenericUtils.safeCast(intObj, Integer.class);
        Optional<Double> failedResult = GenericUtils.safeCast(stringObj, Double.class);
        
        // Then
        assertTrue(stringResult.isPresent());
        assertEquals("Hello", stringResult.get());
        assertTrue(intResult.isPresent());
        assertEquals(Integer.valueOf(42), intResult.get());
        assertFalse(failedResult.isPresent());
    }

    @Test
    void testTypeSafeMap() {
        // Given
        GenericUtils.TypeSafeMap map = new GenericUtils.TypeSafeMap();
        
        // When
        map.put(String.class, "Hello");
        map.put(Integer.class, 42);
        
        // Then
        assertEquals("Hello", map.get(String.class));
        assertEquals(Integer.valueOf(42), map.get(Integer.class));
        assertNull(map.get(Double.class));
    }

    @Test
    void testTypeSafeMapWithNull() {
        // Given
        GenericUtils.TypeSafeMap map = new GenericUtils.TypeSafeMap();
        
        // Then
        assertThrows(NullPointerException.class, () -> map.put(null, "value"));
    }

    @Test
    void testComplexTransformation() {
        // Given
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // When
        List<String> transformed = GenericUtils.findAndTransform(
            numbers,
            n -> n % 2 == 0,
            n -> "Even: " + n
        );
        
        // Then
        assertEquals(Arrays.asList("Even: 2", "Even: 4"), transformed);
    }
} 