package com.github.marschall.acme.money;

import static org.junit.Assert.assertEquals;
import static com.github.marschall.acme.money.FractionMath.gcd;

import org.junit.Test;

public class FractionMathTest {

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
