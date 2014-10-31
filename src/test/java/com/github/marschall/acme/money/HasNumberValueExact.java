package com.github.marschall.acme.money;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HasNumberValueExact extends TypeSafeMatcher<FractionValue> {
  
  private final Number value;

  private HasNumberValueExact(Number value) {
    this.value = value;
  }
  
  @Factory
  public static Matcher<FractionValue> hasNumberValueExcat(Number value) {
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
      return item.numberValueExact(this.value.getClass()).equals(this.value);
    } catch (ArithmeticException e) {
      return false;
    }
  }

}
