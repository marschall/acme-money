package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.FractionMath.gcd;
import static com.github.marschall.acme.money.FractionMath.toLongExact;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FractionMathTest {

  @Test
  void testToLongExact() {
    double[] invalidDoubles = new double[] {
        Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY,
        Double.NaN,
        Double.MAX_VALUE
    };
    for (double value : invalidDoubles) {
      assertThrows(ArithmeticException.class, () -> toLongExact(value));
    }
  }

  @Test
  void testGcd() {
    assertEquals(12L, gcd(420L, 96L));
    assertEquals(12L, gcd(96L, 420L));
    assertEquals(12L, gcd(-420L, 96L));
    assertEquals(12L, gcd(-96L, 420L));

    assertEquals(1L, gcd(96L, 1L));
    assertEquals(1L, gcd(1L, 96L));

    assertEquals(1L, gcd(101L, 23L));
    assertEquals(1L, gcd(23L, 101L));
  }

}
