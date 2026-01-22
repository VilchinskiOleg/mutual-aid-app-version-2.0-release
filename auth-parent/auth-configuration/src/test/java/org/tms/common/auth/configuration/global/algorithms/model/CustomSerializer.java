package org.tms.common.auth.configuration.global.algorithms.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import lombok.SneakyThrows;

public class CustomSerializer {

  public static void main(String[] args) {
    var rout = new Rout(15896729, "Warsaw", "Berlin", 571);
    CustomSerializer serializer = new CustomSerializer();

    // 1. Classic approach :
    byte[] bytes1 = serializer.serializeEntireObj(rout);
    System.out.println("Serialize entire Object. Byte array size : " + bytes1.length);
    Rout restoredRout1 = serializer.deserializeEntireObj(bytes1);

    // 2. Optimised. Serialize only fields :
    byte[] bytes2 = serializer.serializeFieldValues(rout);
    System.out.println("Serialize only field values. Byte array size : " + bytes2.length);
    Rout restoredRout2 = serializer.deserializeFieldsValues(bytes2);

    System.out.println("OK");
  }


  @SneakyThrows
  public byte[] serializeFieldValues(Rout rout) {
    ByteArrayOutputStream BAOStr = new ByteArrayOutputStream();
    DataOutputStream DOStr = new DataOutputStream(BAOStr);

    DOStr.writeLong(rout.id);
    DOStr.writeUTF(rout.source);
    DOStr.writeUTF(rout.destination);
    DOStr.writeInt(rout.distance);

    return BAOStr.toByteArray();
  }

  @SneakyThrows
  public Rout deserializeFieldsValues(byte[] rout) {
    ByteArrayInputStream BAIStr = new ByteArrayInputStream(rout);
    DataInputStream DIStr = new DataInputStream(BAIStr);

    // Can just write appropriate types one by one [order is important!] :
    long routId = DIStr.readLong();
    String routSrc = DIStr.readUTF();
    String routDst = DIStr.readUTF();
    int distance = DIStr.readInt();

    return new Rout(routId, routSrc, routDst, distance);
  }


  @SneakyThrows
  public byte[] serializeEntireObj(Rout rout) {
    ByteArrayOutputStream BAOStr = new ByteArrayOutputStream();
    ObjectOutputStream objOutStr = new ObjectOutputStream(BAOStr);

    objOutStr.writeObject(rout);
    return BAOStr.toByteArray();
  }

  @SneakyThrows
  public Rout deserializeEntireObj(byte[] rout) {
    ByteArrayInputStream BAIStr = new ByteArrayInputStream(rout);
    ObjectInputStream objInStr = new ObjectInputStream(BAIStr);

    return (Rout) objInStr.readObject();
  }


  public static class Rout implements Serializable {

    private final long id;
    private final String source;
    private final String destination;
    private final int distance;

    public Rout(long id, String source, String destination, int distance) {
      this.id = id;
      this.source = source;
      this.destination = destination;
      this.distance = distance;
    }
  }
}
