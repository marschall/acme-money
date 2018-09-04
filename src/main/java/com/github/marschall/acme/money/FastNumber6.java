package com.github.marschall.acme.money;

import java.io.ObjectStreamException;

public final class FastNumber6 extends Number implements Comparable<FastNumber6> {

  private static final long serialVersionUID = 2L;

  final long value;

  FastNumber6(long value) {
    this.value = value;
  }

  @Override
  public int intValue() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long longValue() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float floatValue() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double doubleValue() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String toString() {
    return DecimalMath.fastNumber6ToString(this.value);
  }

  @Override
  public int compareTo(FastNumber6 other) {
    if (other == this) {
      return 0;
    }
    return Long.compare(this.value, other.value);
  }

  private Object writeReplace() throws ObjectStreamException {
    return new Ser(this);
  }

}
