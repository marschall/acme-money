package com.github.marschall.acme.money;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

final class HasNoNumberValue extends TypeSafeMatcher<FractionValue> {
  
  private final Class<? extends Number> clazz;
  
  private HasNoNumberValue(Class<? extends Number> clazz) {
    this.clazz = clazz;
  }
  
  @Factory
  static Matcher<FractionValue> hasNoNumberValue(Class<? extends Number> clazz) {
    return new HasNoNumberValue(clazz);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has no number value of type" + this.clazz);
  }

  @Override
  protected boolean matchesSafely(FractionValue item) {
    try {
      item.numberValue(this.clazz);
      return false;
    } catch (ArithmeticException e) {
      return true;
    }
  }

}
