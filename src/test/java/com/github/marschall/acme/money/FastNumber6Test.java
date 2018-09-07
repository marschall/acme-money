package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class FastNumber6Test {

  @Test
  void byteValue() {
    fail("Not yet implemented");
  }

  @Test
  void shortValue() {
    fail("Not yet implemented");
  }

  @Test
  void intValue() {
    fail("Not yet implemented");
  }

  @Test
  void longValue() {
    fail("Not yet implemented");
  }

  @Test
  void floatValue() {
    fail("Not yet implemented");
  }

  @Test
  void doubleValue() {
    fail("Not yet implemented");
  }

  @Test
  void testEquals() {
    FastNumber6 zero = new FastNumber6(0L);
    FastNumber6 one = new FastNumber6(1_000000L);

    assertEquals(one, one);
    assertEquals(one, new FastNumber6(1_000000L));
    assertNotEquals(one, zero);
  }

  @Test
  void testToString() {
    assertEquals("0.000000", new FastNumber6(0L).toString());
    assertEquals("0.000001", new FastNumber6(1L).toString());
    assertEquals("-0.000001", new FastNumber6(-1L).toString());
    assertEquals("1.000000", new FastNumber6(1_000000L).toString());
    assertEquals("-1.000000", new FastNumber6(-1_000000L).toString());
  }

  @Test
  void compareTo() {
    FastNumber6 zero = new FastNumber6(0L);
    FastNumber6 one = new FastNumber6(1_000000L);

    assertThat(zero, comparesEqualTo(zero));
    assertThat(zero, lessThan(one));
    assertThat(zero, lessThanOrEqualTo(one));
    assertThat(one, greaterThan(zero));
    assertThat(one, greaterThanOrEqualTo(zero));
  }

  @Test
  void serialize() throws ClassNotFoundException, IOException {
    FastNumber6 number = new FastNumber6(123456789L);
    assertEquals(number, SerializationUtil.serializeCopy(number));
  }

}
