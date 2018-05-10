package com.github.marschall.acme.money;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FractionTest {

  @Test
  void testEquals() {
    assertEquals(Fraction.of(1, 2), Fraction.of(1, 2));
  }

  @Test
  void normalizatin() {
    Fraction f = Fraction.of(-1, -2);
    assertEquals(1L, f.getNumerator());
    assertEquals(2L, f.getDenominator());

    f = Fraction.of(1, -2);
    assertEquals(-1L, f.getNumerator());
    assertEquals(2L, f.getDenominator());
  }

  @Test
  void gdc() {
    Fraction f = Fraction.of(2, 4);
    assertEquals(1L, f.getNumerator());
    assertEquals(2L, f.getDenominator());

    f = Fraction.of(4, 2);
    assertEquals(2L, f.getNumerator());
    assertEquals(1L, f.getDenominator());
  }

  @Test
  void compareTo() {
    Fraction f1 = Fraction.of(3, 5); // 0.6
    Fraction f2 = Fraction.of(2, 3); // 0.66
    Fraction f3 = Fraction.of(1, 2); // 0.5

    assertEquals(0, f1.compareTo(f1));
    assertEquals(-1, f1.compareTo(f2));
    assertEquals(1, f2.compareTo(f1));
    assertEquals(1, f2.compareTo(f3));
    assertEquals(-1, f3.compareTo(f2));
    assertEquals(1, f1.compareTo(f3));
    assertEquals(-1, f3.compareTo(f1));

    Fraction f4 = Fraction.of(-3, 5); // -0.6
    Fraction f5 = Fraction.of(-2, 3); // -0.66

    assertEquals(1, f1.compareTo(f4));
    assertEquals(-1, f4.compareTo(f1));
    assertEquals(0, f4.compareTo(f4));
    assertEquals(1, f4.compareTo(f5));
    assertEquals(-1, f5.compareTo(f4));
  }

}
