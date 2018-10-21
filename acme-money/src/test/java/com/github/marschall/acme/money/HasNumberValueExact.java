package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.NumberEquality.numberEquals;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

final class HasNumberValueExact extends TypeSafeMatcher<FractionValue> {
  
  private final Number value;

  private HasNumberValueExact(Number value) {
    this.value = value;
  }
  
  @Factory
  static Matcher<FractionValue> hasNumberValueExcat(Number value) {
    return new HasNumberValueExact(value);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has number value of type " + this.value.getClass() + " with exact value ");
    description.appendValue(this.value);
  }

  @Override
  protected boolean matchesSafely(FractionValue item) {
    try {
      Number actual = item.numberValueExact(this.value.getClass());
      return numberEquals(actual, this.value);
    } catch (ArithmeticException e) {
      return false;
    }
  }

}
