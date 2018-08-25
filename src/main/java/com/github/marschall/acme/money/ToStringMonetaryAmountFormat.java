package com.github.marschall.acme.money;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatContext;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryParseException;

/**
 * Formats and parses a text string such as 'EUR 25.25'.
 */
final class ToStringMonetaryAmountFormat implements MonetaryAmountFormat {

  private final ToStringMonetaryAmountFormatStyle style;

  private ToStringMonetaryAmountFormat(ToStringMonetaryAmountFormatStyle style) {
    this.style = Objects.requireNonNull(style);
  }

  static ToStringMonetaryAmountFormat of(ToStringMonetaryAmountFormatStyle style) {
    return new ToStringMonetaryAmountFormat(style);
  }

  @Override
  public String queryFrom(MonetaryAmount amount) {
    Objects.requireNonNull(amount, "amount");
    return amount.toString();
  }

  @Override
  public AmountFormatContext getContext() {
    throw new UnsupportedOperationException(
        "ToStringMonetaryAmountFormat does not the method suport getAmountFormatContext()");
  }

  @Override
  public void print(Appendable appendable, MonetaryAmount amount) throws IOException {
    Objects.requireNonNull(amount, "amount");
    if (amount instanceof FastMoney6) {
      ((FastMoney6) amount).toStringOn(appendable);
    } else {
      appendable.append(amount.toString());
    }
  }

  @Override
  public MonetaryAmount parse(CharSequence text) throws MonetaryParseException {
    ParsedMonetaryAmount amount = this.parserMonetaryAmount(text);
    return this.style.to(amount);
  }

  private ParsedMonetaryAmount parserMonetaryAmount(CharSequence text) {
    String[] array = Objects.requireNonNull(text).toString().split(" ");
    CurrencyUnit currencyUnit = Monetary.getCurrency(array[0]);
    BigDecimal number = new BigDecimal(array[1]);
    return new ParsedMonetaryAmount(currencyUnit, number);
  }

  static final class ParsedMonetaryAmount {

    ParsedMonetaryAmount(CurrencyUnit currencyUnit, BigDecimal number) {
      this.currencyUnit = currencyUnit;
      this.number = number;
    }

    final CurrencyUnit currencyUnit;

    final BigDecimal number;

  }

  /**
   * indicates with implementation will used to format or parser in
   * ToStringMonetaryAmountFormat
   */
  enum ToStringMonetaryAmountFormatStyle {
    FAST_MONEY_6 {
      @Override
      MonetaryAmount to(ParsedMonetaryAmount amount) {
        return FastMoney6.of(amount.number, amount.currencyUnit);
      }
    };

    abstract MonetaryAmount to(ParsedMonetaryAmount amount);
  }

}
