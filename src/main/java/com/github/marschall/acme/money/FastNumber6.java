package com.github.marschall.acme.money;

public final class FastNumber6 extends Number implements Comparable<FastNumber6> {

  private static final long serialVersionUID = 1L;

  private final long value;

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
  public int compareTo(FastNumber6 other) {
    if (other == this) {
      return 0;
    }
    return Long.compare(this.value, other.value);
  }

}
