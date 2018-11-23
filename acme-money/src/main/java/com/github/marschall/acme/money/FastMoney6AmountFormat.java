package com.github.marschall.acme.money;

import java.io.IOException;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatContext;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryParseException;

final class FastMoney6AmountFormat implements MonetaryAmountFormat {
  
  private static final long INTEGER_PART_MULTIPLIER = DecimalMath.pow10(1, FastMoney6.SCALE);

  static MonetaryAmountFormat INSTANCE = new FastMoney6AmountFormat();

  @Override
  public String queryFrom(MonetaryAmount amount) {
    Objects.requireNonNull(amount, "amount");
    return amount.toString();
  }

  @Override
  public AmountFormatContext getContext() {
    // TODO
    throw new UnsupportedOperationException(
        "FastMoney6AmountFormat does not the method suport getAmountFormatContext()");
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
    
    int textLength = text.length();
    boolean negative = textLength > spaceIndex + 1 && text.charAt(spaceIndex + 1) == '-';
    
    int start = negative ? spaceIndex + 2 : spaceIndex + 1;
    int dotIndex = findDotIndex(text, start);
    
    int integerPartLength = dotIndex == -1 ? textLength - start : dotIndex - start;
    if (integerPartLength == 0) {
      throw new MonetaryParseException("integer part expected", text, textLength - 1);
    } else if (integerPartLength > FastMoney6.PRECISION - FastMoney6.SCALE) {
      throw new MonetaryParseException("amount exceeded", text, start + FastMoney6.PRECISION - FastMoney6.SCALE);
    }
    
    long integerPart = parseIntoStartingAt(text, 0L, start, integerPartLength) * INTEGER_PART_MULTIPLIER;
    long fractionPart = parseFractionPart(text, textLength, dotIndex);
    
    long fastValue6 = integerPart + fractionPart;
    
    if (negative) {
      fastValue6 = -fastValue6;
    }
    
    CurrencyUnit currency = Monetary.getCurrency(text.subSequence(0, spaceIndex).toString());
    return new FastMoney6(fastValue6, currency);
  }

  private long parseFractionPart(CharSequence text, int textLength, int dotIndex) {
    if (dotIndex != -1) {
      if (dotIndex == textLength - 1) {
        throw new MonetaryParseException("fraction part expected", text, dotIndex);
      }
      int start = dotIndex + 1;
      int fractionLength = textLength - start;
      if (fractionLength > FastMoney6.SCALE) {
        throw new MonetaryParseException("fraction part too long", text, textLength - 1);
      }
      long fractionPart = parseIntoStartingAt(text, 0L, start, fractionLength);
      if (fractionLength < FastMoney6.SCALE) {
        fractionPart *= DecimalMath.pow10(1, FastMoney6.SCALE - fractionLength);
      }
      return fractionPart;
    } else {
      return 0L;
    }
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
  
  private int findDotIndex(CharSequence text, int start) {
    for (int i = start; i < text.length(); i++) {
      if (text.charAt(i) == '.') {
        return i;
      }
    }
    return -1;
  }

  private int findSpaceIndex(CharSequence text) {
    int textLenght = text.length();
    for (int i = 0; i < textLenght; i++) {
      if (text.charAt(i) == ' ') {
        return i;
      }
    }
    throw new MonetaryParseException("expected space character", text, 0);
  }

}
