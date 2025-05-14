package com.codestorykh.basic;

import com.codestorykh.generics.basic.Box;
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
 * Test class for demonstrating basic generic Box functionality.
 * Tests cover creation, manipulation, and transformation of Box contents.
 */
@DisplayName("Box Generic Container Tests")
class BoxTest {

    private Box<String> stringBox;

    @BeforeEach
    void setUp() {
        stringBox = new Box<>();
        Box<Integer> integerBox = new Box<>();
    }

    @Nested
    @DisplayName("Box Creation Tests")
    class BoxCreationTests {
        
        @Test
        @DisplayName("Should create empty box")
        void shouldCreateEmptyBox() {
            assertAll(
                () -> assertNull(stringBox.getContent()),
                () -> assertFalse(stringBox.hasContent()),
                () -> assertEquals("empty box", stringBox.toString())
            );
        }

        @Test
        @DisplayName("Should create box with content")
        void shouldCreateBoxWithContent() {
            Box<String> box = new Box<>("Hello Generics");
            assertAll(
                () -> assertEquals("Hello Generics", box.getContent()),
                () -> assertTrue(box.hasContent()),
                () -> assertEquals("Hello Generics", box.toString())
            );
        }

        @ParameterizedTest(name = "Should create box with {0}")
        @ValueSource(strings = {"Test", "Hello", "World"})
        void shouldCreateBoxWithDifferentStrings(String content) {
            Box<String> box = new Box<>(content);
            assertEquals(content, box.getContent());
        }
    }

    @Nested
    @DisplayName("Box Content Management Tests")
    class BoxContentManagementTests {

        @Test
        @DisplayName("Should clear box content")
        void shouldClearBoxContent() {
            // Given
            Box<Double> box = new Box<>(3.14);
            assertTrue(box.hasContent());
            
            // When
            box.clear();
            
            // Then
            assertAll(
                () -> assertNull(box.getContent()),
                () -> assertFalse(box.hasContent()),
                () -> assertEquals("empty box", box.toString())
            );
        }

        @Test
        @DisplayName("Should update box content")
        void shouldUpdateBoxContent() {
            // Given
            stringBox.setContent("Initial");
            assertEquals("Initial", stringBox.getContent());
            
            // When
            stringBox.setContent("Updated");
            
            // Then
            assertEquals("Updated", stringBox.getContent());
        }

        @Test
        @DisplayName("Should handle null content")
        void shouldHandleNullContent() {
            // Given
            stringBox.setContent("Content");
            assertTrue(stringBox.hasContent());
            
            // When
            stringBox.setContent(null);
            
            // Then
            assertAll(
                () -> assertNull(stringBox.getContent()),
                () -> assertFalse(stringBox.hasContent())
            );
        }
    }

    @Nested
    @DisplayName("Box Transformation Tests")
    class BoxTransformationTests {

        @ParameterizedTest(name = "Transform string length: {0}")
        @MethodSource("stringLengthTestCases")
        void shouldTransformStringToLength(String input, int expectedLength) {
            // Given
            Box<String> box = new Box<>(input);
            
            // When
            Box<Integer> lengthBox = box.transform(String::length);
            
            // Then
            assertEquals(expectedLength, lengthBox.getContent());
        }

        private static Stream<Arguments> stringLengthTestCases() {
            return Stream.of(
                Arguments.of("Hello", 5),
                Arguments.of("", 0),
                Arguments.of("JUnit", 5),
                Arguments.of("Transformation", 14)
            );
        }

        @Test
        @DisplayName("Should transform with custom transformer")
        void shouldTransformWithCustomTransformer() {
            // Given
            Box<Integer> box = new Box<>(42);
            
            // When
            Box<String> transformed = box.transform(num -> "Number: " + num);
            
            // Then
            assertEquals("Number: 42", transformed.getContent());
        }

        @Test
        @DisplayName("Should handle null in transformation")
        void shouldHandleNullInTransformation() {
            // Given
            Box<String> nullBox = new Box<>();
            
            // When
            Box<Integer> transformed = nullBox.transform(str -> {
                throw new AssertionError("Transformer should not be called for null content");
            });
            
            // Then
            assertNull(transformed.getContent());
        }

        @Test
        @DisplayName("Should chain transformations")
        void shouldChainTransformations() {
            // Given
            Box<String> box = new Box<>("Hello");
            
            // When
            Box<String> result = box
                .transform(String::length)
                .transform(len -> len * 2)
                .transform(num -> "Length doubled: " + num);
            
            // Then
            assertEquals("Length doubled: 10", result.getContent());
        }
    }

    @Nested
    @DisplayName("Box Edge Cases Tests")
    class BoxEdgeCasesTests {

        @Test
        @DisplayName("Should handle maximum integer value")
        void shouldHandleMaxIntegerValue() {
            // Given
            Box<Integer> box = new Box<>(Integer.MAX_VALUE);
            
            // When
            Box<Long> transformed = box.transform(Long::valueOf);
            
            // Then
            assertEquals(Long.valueOf(Integer.MAX_VALUE), transformed.getContent());
        }

        @Test
        @DisplayName("Should handle empty string")
        void shouldHandleEmptyString() {
            // Given
            Box<String> box = new Box<>("");
            
            // Then
            assertAll(
                () -> assertTrue(box.hasContent()),
                () -> assertEquals("", box.getContent()),
                () -> assertEquals(0, box.transform(String::length).getContent())
            );
        }
    }
} 