package com.github.marschall.acme.money;

import java.io.ObjectStreamException;
import java.math.BigDecimal;
import java.math.MathContext;

import javax.money.NumberValue;

final class FastNumberValue6 extends NumberValue {

  private static final long serialVersionUID = 2L;

  final long value;

  FastNumberValue6(long value) {
    this.value = value;
  }

  @Override
  public Class<?> getNumberType() {
    return FastNumber6.class;
  }

  @Override
  public int getPrecision() {
    return 19;
  }

  @Override
  public int getScale() {
    return FastMoney6.SCALE;
  }

  private boolean hasDecimalPlaces() {
    long remainder = this.value % FastMoney6.DIVISOR;
    return remainder != 0;
  }

  private void checkNoDecimalPlaces() {
    if (this.hasDecimalPlaces()) {
      throw new ArithmeticException("decimal places present");
    }
  }

  @Override
  public NumberValue round(MathContext mathContext) {
    // TODO return instance of same class?
    // TODO implement without BigDecimal
    BigDecimal original = DecimalMath.bigDecimal(this.value);
    BigDecimal rounded = original.round(mathContext);
    if (rounded == original) {
      return this;
    } else {
      return new FastNumberValue6(ConvertToFastLong6.fromBigDecimal(rounded));
    }
  }

  @Override
  public int intValueExact() {
    this.checkNoDecimalPlaces();
    return Math.toIntExact(this.value / FastMoney6.DIVISOR);
  }

  @Override
  public long longValueExact() {
    this.checkNoDecimalPlaces();
    return this.value / FastMoney6.DIVISOR;
  }

  @Override
  public <T extends Number> T numberValue(Class<T> numberType) {
    if (numberType == FastNumber6.class) {
      return numberType.cast(new FastNumber6(this.value));
    }
    return ConvertFastNumber6ToNumber.of(numberType, this, this.value);
  }

  @Override
  public <T extends Number> T numberValueExact(Class<T> numberType) {
    if (numberType == FastNumber6.class) {
      return numberType.cast(new FastNumber6(this.value));
    }
    return ConvertFastNumber6ToNumber.ofExact(numberType, this, this.value);
  }

  @Override
  public long getAmountFractionNumerator() {
    return this.value % FastMoney6.DIVISOR;
  }

  @Override
  public long getAmountFractionDenominator() {
    return FastMoney6.DIVISOR;
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
  public double doubleValueExact() {
    double converted = this.doubleValue();
    if ((converted * FastMoney6.DIVISOR) == this.value) {
      return converted;
    } else {
      throw new ArithmeticException("no exact representation");
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof FastNumberValue6)) {
      return false;
    }
    FastNumberValue6 other = (FastNumberValue6) obj;
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

  private Object writeReplace() throws ObjectStreamException {
    return new Ser(this);
  }

}
