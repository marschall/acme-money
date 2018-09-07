package com.github.marschall.acme.money;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

final class SerializationUtil {

  private SerializationUtil() {
    throw new AssertionError("not instantiable");
  }

  static Object serializeCopy(Serializable s) throws IOException, ClassNotFoundException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(s);
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
         ObjectInputStream ois = new ObjectInputStream(bais)) {
      return ois.readObject();
    }
  }

}
