package net.keksipurkki.charles_o_clock.api;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import io.vertx.core.Future;

import java.util.Iterator;
import java.util.Optional;

/**
 * Patch OpenAPI generator to play nice with Vert.x Futures
 */
public class FutureValueSupport implements ModelConverter {

    private Optional<AnnotatedType> futureValue(AnnotatedType type) {
        JavaType _type = Json.mapper().constructType(type.getType());
        return Optional.ofNullable(_type)
                       .filter(t -> Future.class.isAssignableFrom(t.getRawClass()))
                       .map(t -> t.findTypeParameters(t.getRawClass())[0])
                       .map(this::annotatedType);
    }

    private AnnotatedType annotatedType(JavaType type) {
        return new AnnotatedType()
            .type(type)
            .resolveAsRef(true);
    }

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        return Optional.ofNullable(chain.next())
                       .map(c -> c.resolve(futureValue(type).orElse(type), context, chain))
                       .orElse(null);
    }
}
