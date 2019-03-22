package org.art.web.compiler.config.converter;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.art.web.compiler.model.Entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class KryoTest {
    public static void main(String[] args) {
        Kryo kryo = new Kryo();
        kryo.register(Entity.class, 10);
        Entity entity = new Entity("ArtId", 26, "I am binary!".getBytes(StandardCharsets.UTF_8));
//        String entity = "Hello!";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Output output = new Output(out);
        kryo.writeClassAndObject(output, entity);
        output.flush();
//        System.out.println(out.toByteArray().length);

        Kryo kryo1 = new Kryo();
        kryo1.register(Entity.class, 10);
        Entity entity1 = (Entity) kryo1.readClassAndObject(new Input(out.toByteArray()));
        System.out.println(entity1);
        System.out.println(new String(entity1.getBinData(), StandardCharsets.UTF_8));

    }
}
