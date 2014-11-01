package com.github.marschall.acme.money;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

final class HasNoNumberValueExact extends TypeSafeMatcher<FractionValue> {
  
  private final Class<? extends Number> clazz;
  
  private HasNoNumberValueExact(Class<? extends Number> clazz) {
    this.clazz = clazz;
  }
  
  @Factory
  static Matcher<FractionValue> hasNoNumberValueExact(Class<? extends Number> clazz) {
    return new HasNoNumberValueExact(clazz);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has no exact number value of type" + this.clazz);
  }

  @Override
  protected boolean matchesSafely(FractionValue item) {
    try {
      item.numberValueExact(this.clazz);
      return false;
    } catch (ArithmeticException e) {
      return true;
    }
  }

}
