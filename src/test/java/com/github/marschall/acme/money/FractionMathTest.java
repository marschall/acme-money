package com.github.marschall.acme.money;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static com.github.marschall.acme.money.FractionMath.gcd;
import static com.github.marschall.acme.money.FractionMath.toLongExact;

import org.junit.Test;

public class FractionMathTest {
  
  @Test
  public void testToLongExact() {
    double[] invalidDoubles = new double[] {
        Double.POSITIVE_INFINITY,
        Double.NEGATIVE_INFINITY,
        Double.NaN,
        Double.MAX_VALUE
    };
    for (double value : invalidDoubles) {
      try {
        toLongExact(value);
        fail(value + " should be invalid");
      } catch (ArithmeticException e) {
        // should reach here
      }
    }
  }

  @Test
  public void testGcd() {
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
