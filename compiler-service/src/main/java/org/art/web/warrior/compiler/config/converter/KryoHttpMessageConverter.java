package org.art.web.warrior.compiler.config.converter;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import java.io.IOException;

public class KryoHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    private static final MediaType KRYO = new MediaType("application", "x-kryo");

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(CompilationResp.class, 10);
        kryo.register(CompilationUnitResp.class, 11);
        return kryo;
    });

    public KryoHttpMessageConverter() {
        super(KRYO);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Object.class.isAssignableFrom(clazz);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        Input input = new Input(inputMessage.getBody());
        return kryoThreadLocal.get().readClassAndObject(input);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException {
        Output output = new Output(outputMessage.getBody());
        kryoThreadLocal.get().writeClassAndObject(output, o);
        output.flush();
    }

    @Override
    protected MediaType getDefaultContentType(Object o) {
        return KRYO;
    }
}
