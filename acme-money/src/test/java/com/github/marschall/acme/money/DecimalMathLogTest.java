package com.github.marschall.acme.money;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DecimalMathLogTest {

  static Stream<Arguments> intProvider() {
    return Stream.of(
            Arguments.of(0, 1),
            Arguments.of(1, 1),
            Arguments.of(9, 1),
            Arguments.of(10, 2),
            Arguments.of(99, 2),
            Arguments.of(100, 3),
            Arguments.of(999, 3),
            Arguments.of(1000, 4),
            Arguments.of(9999, 4),
            Arguments.of(10000, 5),
            Arguments.of(99999, 5),
            Arguments.of(100000, 6),
            Arguments.of(999999, 6),
            Arguments.of(1000000, 7),
            Arguments.of(9999999, 7),
            Arguments.of(10000000, 8),
            Arguments.of(99999999, 8),
            Arguments.of(100000000, 9),
            Arguments.of(999999999, 9),
            Arguments.of(1000000000, 10),
            Arguments.of(Integer.MAX_VALUE, 10)
            );
  }

  @ParameterizedTest
  @MethodSource("intProvider")
  void testLog10if(int i, int length) {
    assertEquals(length, DecimalMath.lenghtIf(i), () -> "length(" + i + ")");
  }

  @ParameterizedTest
  @MethodSource("intProvider")
  void testLog10loop(int i, int length) {
    assertEquals(length, DecimalMath.lenghtLoop(i), () -> "length(" + i + ")");
  }

}
