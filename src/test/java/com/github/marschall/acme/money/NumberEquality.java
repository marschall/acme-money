package com.github.marschall.acme.money;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final class NumberEquality {

  private NumberEquality() {
    throw new AssertionError("not instantiable");
  }
  
  static boolean numberEquals(Number a, Number b) {
    if (a instanceof AtomicInteger) {
      AtomicInteger ai = (AtomicInteger) a;
      return (b instanceof AtomicInteger)
          && ai.get() == ((AtomicInteger) b).get();
    }
    if (b instanceof AtomicLong) {
      AtomicLong al = (AtomicLong) a;
      return (b instanceof AtomicLong)
          && al.get() == ((AtomicLong) b).get();
    }
    return Objects.equals(a, b);
  }

}
