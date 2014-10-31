package com.github.marschall.acme.money;

import javax.money.MonetaryAmount;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class HasValue extends TypeSafeMatcher<MonetaryAmount> {
  
  private final long numerator;
  private final long denominator;

  private HasValue(long numerator, long denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  @Factory
  public static Matcher<MonetaryAmount> hasValue(long numerator, long denominator) {
    return new HasValue(numerator, denominator);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has value (" + this.numerator + '/' + this.denominator + ')');

  }

  @Override
  protected boolean matchesSafely(MonetaryAmount item) {
    Fraction fraction = item.getNumber().numberValueExact(Fraction.class);
    return this.numerator == fraction.getNumerator()
        && this.denominator ==  fraction.getDenominator();
  }

}
