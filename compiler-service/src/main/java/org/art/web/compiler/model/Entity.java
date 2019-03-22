package org.art.web.compiler.model;

public class Entity {

    public Entity(){}

    public Entity(String id, int age, byte[] binData) {
        this.id = id;
        this.age = age;
        this.binData = binData;
    }

    private String id;
    private int age;
    private byte[] binData;

    public byte[] getBinData() {
        return binData;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id='" + id + '\'' +
                ", age=" + age +
                '}';
    }
}
