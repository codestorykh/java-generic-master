package com.codestorykh.bounded;

import com.codestorykh.generics.bounded.NumberBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for demonstrating bounded type parameters with NumberBox.
 * Tests cover numeric operations, comparisons, and type safety.
 */
@DisplayName("NumberBox Bounded Type Tests")
class NumberBoxTest {

    private NumberBox<Integer> intBox;
    private NumberBox<Double> doubleBox;

    @BeforeEach
    void setUp() {
        intBox = new NumberBox<>(10);
        doubleBox = new NumberBox<>(5.5);
    }

    @Nested
    @DisplayName("Basic Operations Tests")
    class BasicOperationsTests {

        @ParameterizedTest(name = "Square root of {0} should be {1}")
        @MethodSource("squareRootTestCases")
        void shouldCalculateSquareRoot(Number input, double expected) {
            NumberBox<? extends Number> box = new NumberBox<>(input);
            assertEquals(expected, box.sqrt(), 0.0001);
        }

        private static Stream<Arguments> squareRootTestCases() {
            return Stream.of(
                Arguments.of(16, 4.0),
                Arguments.of(4.0, 2.0),
                Arguments.of(2, 1.4142),
                Arguments.of(0, 0.0),
                Arguments.of(0.25, 0.5)
            );
        }

        @Test
        @DisplayName("Should handle getters and setters")
        void shouldHandleGettersAndSetters() {
            // Given
            NumberBox<Double> box = new NumberBox<>(3.14);
            
            // When
            box.setNumber(2.718);
            
            // Then
            assertAll(
                () -> assertEquals(2.718, box.getNumber(), 0.0001),
                () -> assertEquals("2.718", box.toString())
            );
        }
    }

    @Nested
    @DisplayName("Comparison Operations Tests")
    class ComparisonTests {

        @Test
        @DisplayName("Should compare numbers correctly")
        void shouldCompareNumbersCorrectly() {
            assertAll(
                () -> assertTrue(intBox.isGreaterThan(doubleBox), "10 should be greater than 5.5"),
                () -> assertFalse(intBox.isLessThan(doubleBox), "10 should not be less than 5.5"),
                () -> assertTrue(doubleBox.isLessThan(intBox), "5.5 should be less than 10"),
                () -> assertFalse(doubleBox.isGreaterThan(intBox), "5.5 should not be greater than 10")
            );
        }

        @ParameterizedTest(name = "Compare {0} with {1}")
        @MethodSource("comparisonTestCases")
        void shouldHandleVariousComparisons(Number a, Number b, boolean expectedGreater) {
            NumberBox<? extends Number> boxA = new NumberBox<>(a);
            NumberBox<? extends Number> boxB = new NumberBox<>(b);
            
            // For equal numbers, both isGreaterThan and isLessThan should be false
            boolean areEqual = a.doubleValue() == b.doubleValue();
            
            // Test greater than
            assertEquals(expectedGreater, boxA.isGreaterThan(boxB), 
                String.format("Expected %s %s %s", a, expectedGreater ? ">" : "≤", b));
            
            // Test less than - if numbers are equal, should be false
            if (areEqual) {
                assertFalse(boxA.isLessThan(boxB), 
                    String.format("Expected %s to not be less than %s (equal numbers)", a, b));
            } else {
                assertEquals(!expectedGreater, boxA.isLessThan(boxB),
                    String.format("Expected %s %s %s", a, !expectedGreater ? "<" : "≥", b));
            }
        }

        private static Stream<Arguments> comparisonTestCases() {
            return Stream.of(
                Arguments.of(10, 5, true),      // 10 > 5
                Arguments.of(5, 10, false),     // 5 < 10
                Arguments.of(5.5, 5.5, false),  // equal numbers - neither greater nor less
                Arguments.of(1000L, 999.9, true),  // 1000 > 999.9
                Arguments.of(0.1f, 0.2, false)  // 0.1 < 0.2
            );
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations Tests")
    class ArithmeticTests {

        @Test
        @DisplayName("Should perform basic arithmetic operations")
        void shouldPerformBasicArithmeticOperations() {
            assertAll(
                () -> assertEquals(15.5, intBox.add(doubleBox), 0.0001),
                () -> assertEquals(4.5, intBox.subtract(doubleBox), 0.0001),
                () -> assertEquals(55.0, intBox.multiply(doubleBox), 0.0001),
                () -> assertEquals(1.818181, intBox.divide(doubleBox), 0.0001)
            );
        }

        @ParameterizedTest(name = "Arithmetic operations with different number types")
        @MethodSource("mixedNumberTypeTestCases")
        void shouldHandleMixedNumberTypes(Number a, Number b) {
            NumberBox<? extends Number> boxA = new NumberBox<>(a);
            NumberBox<? extends Number> boxB = new NumberBox<>(b);
            
            assertAll(
                () -> assertDoesNotThrow(() -> boxA.add(boxB)),
                () -> assertDoesNotThrow(() -> boxA.subtract(boxB)),
                () -> assertDoesNotThrow(() -> boxA.multiply(boxB)),
                () -> assertDoesNotThrow(() -> boxA.divide(boxB))
            );
        }

        private static Stream<Arguments> mixedNumberTypeTestCases() {
            return Stream.of(
                Arguments.of(10, 5.5f),
                Arguments.of(15L, 3),
                Arguments.of(20.0, 4L),
                Arguments.of(100f, 10.0)
            );
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle division by zero")
        void shouldHandleDivisionByZero() {
            // Given
            NumberBox<Integer> dividend = new NumberBox<>(10);
            NumberBox<Integer> zero = new NumberBox<>(0);
            
            // Then
            assertThrows(ArithmeticException.class, () -> dividend.divide(zero));
        }

        @ParameterizedTest(name = "Should handle extreme values: {0}")
        @ValueSource(doubles = {Double.MAX_VALUE, Double.MIN_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
        void shouldHandleExtremeValues(double value) {
            // Given
            NumberBox<Double> extremeBox = new NumberBox<>(value);
            
            // Then
            assertDoesNotThrow(() -> {
                extremeBox.sqrt();
                extremeBox.add(doubleBox);
                extremeBox.multiply(doubleBox);
            });
        }

        @Test
        @DisplayName("Should handle NaN values")
        void shouldHandleNaN() {
            // Given
            NumberBox<Double> nanBox = new NumberBox<>(Double.NaN);
            
            // Then
            assertTrue(Double.isNaN(nanBox.sqrt()));
        }
    }

    @Nested
    @DisplayName("Type Safety Tests")
    class TypeSafetyTests {

        @Test
        @DisplayName("Should work with different Number subtypes")
        void shouldWorkWithDifferentNumberSubtypes() {
            // Given
            NumberBox<Integer> integerBox = new NumberBox<>(1);
            NumberBox<Double> doubleBox = new NumberBox<>(1.0);
            NumberBox<Long> longBox = new NumberBox<>(1L);
            NumberBox<Float> floatBox = new NumberBox<>(1.0f);
            
            // When - perform additions step by step
            NumberBox<Double> result1 = new NumberBox<>(floatBox.add(longBox));  // Float + Long
            NumberBox<Double> result2 = new NumberBox<>(doubleBox.add(result1)); // Double + previous result
            double finalResult = integerBox.add(result2); // Integer + previous result
            
            // Then
            assertAll(
                "Type safety operations",
                () -> assertEquals(4.0, finalResult, 0.0001, "Sum of all numbers should be 4.0"),
                () -> assertEquals(1.0, doubleBox.getNumber(), 0.0001, "Double value should remain unchanged"),
                () -> assertEquals(1L, longBox.getNumber(), "Long value should remain unchanged"),
                () -> assertEquals(1.0f, floatBox.getNumber(), 0.0001, "Float value should remain unchanged"),
                () -> assertEquals(1, integerBox.getNumber(), "Integer value should remain unchanged")
            );
        }

        @Test
        @DisplayName("Should handle type-specific operations")
        void shouldHandleTypeSpecificOperations() {
            // Given
            NumberBox<Integer> intBox = new NumberBox<>(5);
            NumberBox<Double> doubleBox = new NumberBox<>(5.0);
            NumberBox<Long> longBox = new NumberBox<>(5L);
            
            // When & Then
            assertAll(
                "Type-specific arithmetic operations",
                () -> assertEquals(10.0, intBox.add(doubleBox), 0.0001, "Integer + Double addition"),
                () -> assertEquals(10.0, doubleBox.add(longBox), 0.0001, "Double + Long addition"),
                () -> assertEquals(10.0, longBox.add(intBox), 0.0001, "Long + Integer addition")
            );
        }

        @Test
        @DisplayName("Should preserve type information")
        void shouldPreserveTypeInformation() {
            // Given
            NumberBox<Integer> intBox = new NumberBox<>(10);
            NumberBox<Double> doubleBox = new NumberBox<>(10.0);
            
            // When & Then
            assertAll(
                "Type preservation checks",
                () -> assertInstanceOf(Integer.class, intBox.getNumber(), "Should preserve Integer type"),
                () -> assertInstanceOf(Double.class, doubleBox.getNumber(), "Should preserve Double type"),
                () -> assertEquals(Integer.valueOf(10), intBox.getNumber(), "Should maintain Integer value"),
                () -> assertEquals(10.0, doubleBox.getNumber(), 0.0001, "Should maintain Double value")
            );
        }

        @Test
        @DisplayName("Should handle mixed type comparisons")
        void shouldHandleMixedTypeComparisons() {
            // Given
            NumberBox<Integer> intBox = new NumberBox<>(10);
            NumberBox<Double> doubleBox = new NumberBox<>(10.0);
            NumberBox<Long> longBox = new NumberBox<>(10L);
            
            // Then
            assertAll(
                "Mixed type comparisons",
                () -> assertFalse(intBox.isGreaterThan(doubleBox), "10 should not be greater than 10.0"),
                () -> assertFalse(intBox.isLessThan(doubleBox), "10 should not be less than 10.0"),
                () -> assertFalse(doubleBox.isGreaterThan(longBox), "10.0 should not be greater than 10L"),
                () -> assertFalse(doubleBox.isLessThan(longBox), "10.0 should not be less than 10L")
            );
        }

        @Test
        @DisplayName("Should handle precision in type conversions")
        void shouldHandlePrecisionInTypeConversions() {
            // Given
            NumberBox<Integer> intBox = new NumberBox<>(1);
            NumberBox<Double> doubleBox = new NumberBox<>(1.1);
            NumberBox<Float> floatBox = new NumberBox<>(1.1f);
            
            // Then
            assertAll(
                "Precision handling in type conversions",
                () -> assertEquals(2.1, intBox.add(doubleBox), 0.0001, "Integer + Double precision"),
                () -> assertEquals(2.2, doubleBox.add(doubleBox), 0.0001, "Double + Double precision"),
                () -> assertEquals(2.2f, floatBox.add(floatBox), 0.0001, "Float + Float precision"),
                () -> assertTrue(
                    Math.abs(floatBox.add(doubleBox) - 2.2) < 0.0001, 
                    "Float + Double should handle precision correctly"
                )
            );
        }
    }
} 