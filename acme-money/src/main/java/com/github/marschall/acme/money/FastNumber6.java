package com.github.marschall.acme.money;

import java.io.ObjectStreamException;

public final class FastNumber6 extends Number implements Comparable<FastNumber6> {

  private static final long serialVersionUID = 2L;

  final long value;

  FastNumber6(long value) {
    this.value = value;
  }

  /**
   * Obtains an instance of {@link FastNumber6} from a text string such as '25.25'.
   *
   * @param text the text to parse not null
   * @return FastNumber6 instance
   * @throws NullPointerException
   */
  public static FastNumber6 parse(CharSequence text) {
    return new FastNumber6(FastMoney6AmountFormat.parseFastValue6(text, 0, text.length()));
  }

  @Override
  public byte byteValue() {
    return (byte) this.longValue();
  }

  @Override
  public short shortValue() {
    return (short) this.longValue();
  }

  @Override
  public int intValue() {
    return (int) this.longValue();
  }

  @Override
  public long longValue() {
    return this.value / FastMoney6.DIVISOR;
  }

  @Override
  public float floatValue() {
    return (float) this.doubleValue();
  }

  @Override
  public double doubleValue() {
    return DecimalMath.doubleValue(this.value);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof FastNumber6)) {
      return false;
    }
    FastNumber6 other = (FastNumber6) obj;
    return this.value == other.value;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(this.value);
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
