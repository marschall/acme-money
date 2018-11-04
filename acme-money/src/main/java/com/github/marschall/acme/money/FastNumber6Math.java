package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.money.NumberValue;

import com.github.marschall.acme.money.ConvertFractionToNumber.FastNumber6Converter;

final class FastNumber6Math {

  private FastNumber6Math() {
    throw new AssertionError("not instantiable");
  }

  static NumberAccessor getAccessor(Class<?> numberType) {
    NumberAccessor accessor = ACCESSOR_MAP.get(numberType);
    if ((accessor == null) && NumberValue.class.isAssignableFrom(numberType)) {
      accessor = ConvertNumberValue.INSTANCE;
    }
    if (accessor == null) {
      throw new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return accessor;
  }

  interface NumberAccessor {

    boolean isOne(Number number);

    long convertToNumber6(Number number);

    BigDecimal convertToBigDecimal(Number number);

  }

  static long fromBigDecimal(BigDecimal bigDecimal) {
    if (bigDecimal.scale() > FastMoney6.SCALE) {
      // strip trailing zeros only if we have to
      bigDecimal = bigDecimal.stripTrailingZeros();
      if (bigDecimal.scale() > FastMoney6.SCALE) {
        throw scaleTooBig(bigDecimal);
      }
    }
    if (bigDecimal.compareTo(FastMoney6.MIN_BD) < 0) {
      throw valueTooSmall(bigDecimal);
    } else if (bigDecimal.compareTo(FastMoney6.MAX_BD) > 0) {
      throw valueTooLarge(bigDecimal);
    }
    return bigDecimal.movePointRight(FastMoney6.SCALE).longValue();
  }

  static ArithmeticException scaleTooBig(Number number) {
    return new ArithmeticException(number + " can not be represented by this class, scale > " + FastMoney6.SCALE);
  }

  static ArithmeticException valueTooLarge(Number number) {
    return new ArithmeticException("Overflow: " + number + " > " + FastMoney6.MAX_BD);
  }

  static ArithmeticException valueTooSmall(Number number) {
    return new ArithmeticException("Overflow: " + number + " < " + FastMoney6.MIN_BD);
  }

  static final class ConvertDouble implements NumberAccessor {

    @Override
    public long convertToNumber6(Number number) {
      double doubleValue = number.doubleValue() * FastMoney6.DIVISOR;
      if (doubleValue > FastMoney6.MAX_DOUBLE) {
        throw valueTooLarge(number);
      }
      if (doubleValue < FastMoney6.MIN_DOUBLE) {
        throw valueTooSmall(number);
      }
      return Math.round(doubleValue);
    }

    @Override
    public boolean isOne(Number number) {
      return number.doubleValue() == 1.0d;
    }

    @Override
    public BigDecimal convertToBigDecimal(Number number) {
      return BigDecimal.valueOf((double) number);
    }

  }

  static final class ConvertLong implements NumberAccessor {

    @Override
    public long convertToNumber6(Number number) {
      return Math.multiplyExact(number.longValue(), FastMoney6.DIVISOR);
    }

    @Override
    public boolean isOne(Number number) {
      return number.longValue() == 1L;
    }

    @Override
    public BigDecimal convertToBigDecimal(Number number) {
      return BigDecimal.valueOf(number.longValue());
    }

  }

  static final class ConvertFastNumber6 implements NumberAccessor {

    private static final long ONE = DecimalMath.pow10(1, FastMoney6.SCALE);

    @Override
    public long convertToNumber6(Number number) {
      return ((FastNumber6) number).value;
    }

    @Override
    public boolean isOne(Number number) {
      return ((FastNumber6) number).value == ONE;
    }

    @Override
    public BigDecimal convertToBigDecimal(Number number) {
      return DecimalMath.bigDecimal(((FastNumber6) number).value);
    }

  }

  static final class ConvertFastNumberValue6 implements NumberAccessor {

    private static final long ONE = DecimalMath.pow10(1, FastMoney6.SCALE);

    @Override
    public long convertToNumber6(Number number) {
      return ((FastNumberValue6) number).value;
    }

    @Override
    public boolean isOne(Number number) {
      return ((FastNumberValue6) number).value == ONE;
    }

    @Override
    public BigDecimal convertToBigDecimal(Number number) {
      return DecimalMath.bigDecimal(((FastNumberValue6) number).value);
    }

  }

  static final class ConvertFraction implements NumberAccessor {

    private static final Fraction ONE = Fraction.of(1L, 1L);

    @Override
    public long convertToNumber6(Number number) {
      return ((Fraction) number).fastNumberValue6();
    }

    @Override
    public boolean isOne(Number number) {
      return number.equals(ONE);
    }

    @Override
    public BigDecimal convertToBigDecimal(Number number) {
      Fraction fraction = (Fraction) number;
      FastNumber6Converter converter = ConvertFractionToNumber.ConvertToBigDecimal.INSTANCE;
      return (BigDecimal) converter.convertExact(fraction.numerator, fraction.denominator);
    }

  }

  static final class ConvertBigDecimal implements NumberAccessor {

    @Override
    public long convertToNumber6(Number number) {
      return fromBigDecimal((BigDecimal) number);
    }

    @Override
    public boolean isOne(Number number) {
      return ((BigDecimal) number).compareTo(BigDecimal.ONE) == 0;
    }

    @Override
    public BigDecimal convertToBigDecimal(Number number) {
      return (BigDecimal) number;
    }

  }

  static final class ConvertBigInteger implements NumberAccessor {

    private static final BigInteger ONE = BigInteger.valueOf(1L);

    @Override
    public long convertToNumber6(Number number) {
      return Math.multiplyExact(((BigInteger) number).longValueExact(), FastMoney6.DIVISOR);
    }

    @Override
    public boolean isOne(Number number) {
      return number.equals(ONE);
    }

    @Override
    public BigDecimal convertToBigDecimal(Number number) {
      return new BigDecimal((BigInteger) number);
    }

  }

  static final class ConvertNumberValue implements NumberAccessor {

    static final NumberAccessor INSTANCE = new ConvertNumberValue();

    @Override
    public long convertToNumber6(Number number) {
      return fromBigDecimal(((NumberValue) number).numberValueExact(BigDecimal.class));
    }

    @Override
    public boolean isOne(Number number) {
      return ((NumberValue) number).intValueExact() == 1;
    }

    @Override
    public BigDecimal convertToBigDecimal(Number number) {
      return ((NumberValue) number).numberValueExact(BigDecimal.class);
    }

  }

  private static final Map<Class<? extends Number>, NumberAccessor> ACCESSOR_MAP;

  static {
    ACCESSOR_MAP = new HashMap<>(16);
    ACCESSOR_MAP.put(FastNumber6.class, new ConvertFastNumber6());
    ACCESSOR_MAP.put(FastNumberValue6.class, new ConvertFastNumberValue6());
    ACCESSOR_MAP.put(BigInteger.class, new ConvertBigInteger());
    ACCESSOR_MAP.put(BigDecimal.class, new ConvertBigDecimal());
    ACCESSOR_MAP.put(Float.class, new ConvertDouble());
    ACCESSOR_MAP.put(Double.class, new ConvertDouble());
    ACCESSOR_MAP.put(Long.class, new ConvertLong());
    ACCESSOR_MAP.put(Integer.class, new ConvertLong());
    ACCESSOR_MAP.put(Short.class, new ConvertLong());
    ACCESSOR_MAP.put(Byte.class, new ConvertLong());
    ACCESSOR_MAP.put(AtomicInteger.class, new ConvertLong());
    ACCESSOR_MAP.put(AtomicLong.class, new ConvertLong());
    ACCESSOR_MAP.put(Fraction.class, new ConvertFraction());
  }

}
