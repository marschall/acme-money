package com.github.marschall.acme.money;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

final class ConvertsToFraction extends TypeSafeMatcher<Number> {

  private final Fraction fraction;

  private ConvertsToFraction(Fraction fraction) {
    this.fraction = fraction;
  }

  @Factory
  static Matcher<Number> convertsToFraction(Fraction fraction) {
    return new ConvertsToFraction(fraction);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("converts to");
    description.appendValue(this.fraction);
  }

  @Override
  protected boolean matchesSafely(Number item) {
    return ConvertFraction.of(item).equals(this.fraction);
  }

}
