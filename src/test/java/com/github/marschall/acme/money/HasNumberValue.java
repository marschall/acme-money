package com.github.marschall.acme.money;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.github.marschall.acme.money.NumberEquality.numberEquals;

public final class HasNumberValue extends TypeSafeMatcher<FractionValue> {
  
  private final Number value;

  private HasNumberValue(Number value) {
    this.value = value;
  }
  
  @Factory
  public static Matcher<FractionValue> hasNumberValue(Number value) {
    return new HasNumberValue(value);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has number value of type " + this.value.getClass() + " with value ");
    description.appendValue(this.value);
  }

  @Override
  protected boolean matchesSafely(FractionValue item) {
    try {
      Number actual = item.numberValue(this.value.getClass());
      return numberEquals(actual, this.value);
    } catch (ArithmeticException e) {
      return false;
    }
  }

}
