package com.github.marschall.acme.money;

import java.io.IOException;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatContext;
import javax.money.format.AmountFormatContextBuilder;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryParseException;

final class FastMoney6AmountFormat implements MonetaryAmountFormat {

  static final String NAME = "fast6";

  private static final long INTEGER_PART_MULTIPLIER = DecimalMath.pow10(1, FastMoney6.SCALE);

  static MonetaryAmountFormat INSTANCE = new FastMoney6AmountFormat();

  @Override
  public String queryFrom(MonetaryAmount amount) {
    Objects.requireNonNull(amount, "amount");
    return amount.toString();
  }

  @Override
  public AmountFormatContext getContext() {
    AmountFormatQuery query = AmountFormatQueryBuilder.of(NAME).build();
    return AmountFormatContextBuilder.create(query).build();
  }

  @Override
  public void print(Appendable appendable, MonetaryAmount amount) throws IOException {
    if (!(amount instanceof FastMoney6)) {
      throw new IllegalArgumentException("only FastMoney6 is supported");
    }
    FastMoney6 money = (FastMoney6) amount;
    money.toStringOn(appendable);
  }

  @Override
  public MonetaryAmount parse(CharSequence text) throws MonetaryParseException {
    Objects.requireNonNull(text, "text");
    int spaceIndex = findSpaceIndex(text);
    if (spaceIndex == -1) {
      throw new MonetaryParseException("expected space character", text, 0);
    }

    long fastValue6 = parseFastValue6(text, spaceIndex + 1, text.length() - spaceIndex - 1);

    CurrencyUnit currency = Monetary.getCurrency(text.subSequence(0, spaceIndex).toString());
    return new FastMoney6(fastValue6, currency);
  }

  private static long parseFastValue6(CharSequence text, int start, int length) {
    if (start + length > text.length()) {
      throw new IllegalArgumentException();
    }
    if (length <= 0) {
      throw new MonetaryParseException("decimal part expected", text, start);
    }

    boolean negative = text.charAt(start) == '-';

    // start of the unsigned decimal
    int decimalStart = negative ? start + 1 : start;
    int dotIndex = findDotIndex(text, decimalStart, length - (decimalStart - start));

    int integerPartLength = dotIndex == -1 ? length - (decimalStart - start) : dotIndex - decimalStart;
    if (integerPartLength == 0) {
      throw new MonetaryParseException("integer part expected", text, decimalStart);
    } else if (integerPartLength > FastMoney6.PRECISION - FastMoney6.SCALE) {
      throw new MonetaryParseException("amount exceeded", text, decimalStart + FastMoney6.PRECISION - FastMoney6.SCALE);
    }

    long integerPart = parseIntoStartingAt(text, 0L, decimalStart, integerPartLength) * INTEGER_PART_MULTIPLIER;
    long fractionPart = parseFractionPart(text, dotIndex, length - integerPartLength - (decimalStart - start));

    long fastValue6 = integerPart + fractionPart;

    if (negative) {
      fastValue6 = -fastValue6;
    }

    return fastValue6;
  }

  private static long parseFractionPart(CharSequence text, int dotIndex, int length) {
    if (dotIndex != -1) {
      if (length == 1) {
        throw new MonetaryParseException("fraction part expected", text, dotIndex);
      }
      if (length - 1 > FastMoney6.SCALE) {
        throw new MonetaryParseException("fraction part too long", text, dotIndex + length);
      }
      int start = dotIndex + 1;
      return parseScaledFraction(text, start, length - 1);
    } else {
      return 0L;
    }
  }

  private static long parseScaledFraction(CharSequence text, int start, int length) {
    if (length > FastMoney6.SCALE) {
      throw new MonetaryParseException("fraction part too long", text, start + length + 1);
    }
    long fractionPart = parseIntoStartingAt(text, 0L, start, length);
    if (length < FastMoney6.SCALE) {
      fractionPart *= DecimalMath.pow10(1, FastMoney6.SCALE - length);
    }
    return fractionPart;
  }

  private static long parseIntoStartingAt(CharSequence s, long current, int start, int length) {
    long acc = current;
    for (int i = 0; i < length; i++) {
      char c = s.charAt(start + i);
      if (c < '0' || c > '9') {
        throw new MonetaryParseException("not numeric", s, start + i);
      }
      acc = acc * 10 + (c - '0');
    }
    return acc;
  }

  private static int findDotIndex(CharSequence text, int start, int length) {
    for (int i = 0; i < length; i++) {
      if (text.charAt(start + i) == '.') {
        return start + i;
      }
    }
    return -1;
  }

  private static int findSpaceIndex(CharSequence text) {
    int textLenght = text.length();
    for (int i = 0; i < textLenght; i++) {
      if (text.charAt(i) == ' ') {
        return i;
      }
    }
    return -1;
  }

}
