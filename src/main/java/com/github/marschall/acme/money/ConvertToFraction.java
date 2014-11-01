package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.money.NumberValue;

import org.javamoney.moneta.spi.ConvertBigDecimal;
import org.omg.CosNaming.IstringHelper;


enum ConvertToFraction {
  /** Conversion from integral numeric types, short, int, long. */
  INTEGER {
    @Override
    Fraction getFraction(Number num) {
      return Fraction.of(num.longValue(), 1L);
    }
  },
  /** Conversion for floating point numbers. */
  FLUCTUAGE {
    @Override
    Fraction getFraction(Number num) {
      return isScaleZero(new BigDecimal(num.toString()));
    }
  },
  /** Conversion from BigInteger. */
  BIGINTEGER {
    @Override
    Fraction getFraction(Number num) {
      return Fraction.of(((BigInteger) num).longValueExact(), 1L);
    }
  },
  /** Conversion from NumberValue. */
  NUMBERVALUE {
    @Override
    Fraction getFraction(Number num) {
      BigDecimal result = ((NumberValue) num).numberValue(BigDecimal.class);
      return isScaleZero(result);
    }
  },
  FRACTIONVALUE {
    @Override
    Fraction getFraction(Number num) {
      return ((NumberValue) num).numberValue(Fraction.class);
    }
  },
  /** Conversion from BigDecimal. */
  BIGDECIMAL {
    @Override
    Fraction getFraction(Number num) {
      BigDecimal result = (BigDecimal) num;
      return isScaleZero(result);
    }
  },
  /** COnversion from BigDecimal, extended. */
  BIGDECIMAL_EXTENDS {
    @Override
    Fraction getFraction(Number num) {
      BigDecimal result = ((BigDecimal)num).stripTrailingZeros();
      return isScaleZero(result);
    }
  },
  /** Default conversion based on String, if everything else failed. */
  DEFAULT {
    @Override
    Fraction getFraction(Number num) {
      BigDecimal result = null;
      try {
        result = new BigDecimal(num.toString());
      } catch (NumberFormatException ignored) {
      }
      result = Optional.ofNullable(result).orElse(
          BigDecimal.valueOf(num.doubleValue()));
      return isScaleZero(result);
    }
  };


  private static final BigDecimal TEN = BigDecimal.valueOf(10);

  abstract Fraction getFraction(Number num);

  static Fraction of(Number num) {
    Objects.requireNonNull(num, "Number is required.");
    if (num instanceof Fraction) {
      return (Fraction) num;
    }
    return factory(num).getFraction(num);
  }

  private static ConvertToFraction factory(Number num) {
    if (INSTEGERS.contains(num.getClass())) {
      return INTEGER;
    }
    if (FLOATINGS.contains(num.getClass())) {
      return FLUCTUAGE;
    }
    if (num instanceof NumberValue) {
      return NUMBERVALUE;
    }
    if (num instanceof FractionValue) {
      return FRACTIONVALUE;
    }
    if (BigDecimal.class.equals(num.getClass())) {
      return BIGDECIMAL;
    }
    if (num instanceof BigInteger) {
      return BIGINTEGER;
    }
    if (num instanceof BigDecimal) {
      return BIGDECIMAL_EXTENDS;
    }
    return DEFAULT;
  }

  private static List<Class<? extends Number>> INSTEGERS = Arrays.asList(
      Long.class, Integer.class, Short.class, Byte.class,
      AtomicLong.class, AtomicInteger.class);

  private static List<Class<? extends Number>> FLOATINGS = Arrays.asList(
      Float.class, Double.class);

  private static Fraction isScaleZero(BigDecimal d) {
    if (d.signum() == 0) {
      return Fraction.of(0L, 1L);
    }
    BigDecimal decimal = d;
    if (decimal.scale() > 0) {
      decimal = decimal.stripTrailingZeros();
    }
    long denominator = TEN.pow(decimal.scale()).longValueExact();
    long numerator = decimal.movePointRight(decimal.scale()).longValueExact();
    return Fraction.of(numerator, denominator);
  }

}
