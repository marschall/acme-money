package com.github.marschall.acme.money;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

import javax.money.CurrencyUnit;

/**
 * Serialization proxy for all serializable types in this package.
 */
final class Ser implements Externalizable {

  private static final int TYPE_FAST_MONEY_6 = 1;
  private static final int TYPE_FAST_NUMBER_6 = 2;
  private static final int TYPE_FAST_NUMBER_VALUE_6 = 3;

  private static final int TYPE_FRACTION_MONEY = 4;
  private static final int TYPE_FRACTION = 5;
  private static final int TYPE_FRACTION_VALUE = 6;

  private int type;
  private Object value;

  /**
   * Default constructor for serialization.
   */
  public Ser() {
    super();
  }

  Ser(FastMoney6 money) {
    Objects.requireNonNull(money, "money");
    this.type = TYPE_FAST_MONEY_6;
    this.value = money;
  }

  Ser(FastNumber6 number) {
    Objects.requireNonNull(number, "number");
    this.type = TYPE_FAST_NUMBER_6;
    this.value = number;
  }

  Ser(FastNumberValue6 numberValue) {
    Objects.requireNonNull(numberValue, "numberValue");
    this.type = TYPE_FAST_NUMBER_VALUE_6;
    this.value = numberValue;
  }

  Ser(FractionMoney money) {
    Objects.requireNonNull(money, "money");
    this.type = TYPE_FRACTION_MONEY;
    this.value = money;
  }

  Ser(Fraction number) {
    Objects.requireNonNull(number, "number");
    this.type = TYPE_FRACTION;
    this.value = number;
  }

  Ser(FractionValue numberValue) {
    Objects.requireNonNull(numberValue, "numberValue");
    this.type = TYPE_FRACTION_VALUE;
    this.value = numberValue;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeByte(this.type);
    switch (this.type) {
      case TYPE_FAST_MONEY_6:
        out.writeLong(((FastMoney6) this.value).value);
        out.writeObject(((FastMoney6) this.value).currency);
        break;

      case TYPE_FAST_NUMBER_6:
        out.writeLong(((FastNumber6) this.value).value);
        break;

      case TYPE_FAST_NUMBER_VALUE_6:
        out.writeLong(((FastNumberValue6) this.value).value);
        break;

      case TYPE_FRACTION_MONEY:
        out.writeLong(((FractionMoney) this.value).numerator);
        out.writeLong(((FractionMoney) this.value).denominator);
        out.writeObject(((FractionMoney) this.value).currency);
        break;

      case TYPE_FRACTION:
        out.writeLong(((Fraction) this.value).numerator);
        out.writeLong(((Fraction) this.value).denominator);
        break;

      case TYPE_FRACTION_VALUE:
        out.writeLong(((FractionValue) this.value).numerator);
        out.writeLong(((FractionValue) this.value).denominator);
        break;

      default:
        throw new IllegalStateException("unknown type tag: " + this.type);
    }
  }

  @Override
  public void readExternal(ObjectInput in)
          throws IOException, ClassNotFoundException {
    this.type = in.readByte();
    switch (this.type) {
      case TYPE_FAST_MONEY_6:
        this.value = new FastMoney6(in.readLong(), (CurrencyUnit) in.readObject());
        break;

      case TYPE_FAST_NUMBER_6:
        this.value = new FastNumber6(in.readLong());
        break;

      case TYPE_FAST_NUMBER_VALUE_6:
        this.value = new FastNumberValue6(in.readLong());
        break;

      case TYPE_FRACTION_MONEY:
        this.value = new FractionMoney(in.readLong(), in.readLong(), (CurrencyUnit) in.readObject());
        break;

      case TYPE_FRACTION:
        this.value = new Fraction(in.readLong(), in.readLong());
        break;

      case TYPE_FRACTION_VALUE:
        this.value = new FractionValue(in.readLong(), in.readLong());
        break;

      default:
        throw new IllegalStateException("unknown type tag: " + this.type);
    }

  }

  private Object readResolve() {
    return this.value;
  }

}
