package com.github.marschall.acme.money;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConvertFractionTest {

  @Test
  public void doubles() {
    Fraction fraction = ConvertFraction.of(1.5d);
    assertEquals(Fraction.of(3, 2), fraction);
  }

}
