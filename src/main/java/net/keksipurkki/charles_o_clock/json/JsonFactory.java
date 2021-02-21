package net.keksipurkki.charles_o_clock.json;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.EncodeException;
import io.vertx.core.spi.json.JsonCodec;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MetaInfServices
public class JsonFactory implements io.vertx.core.spi.JsonFactory {

    private static final Logger logger = LoggerFactory.getLogger(JsonFactory.class);

    @Override
    public JsonCodec codec() {
        logger.debug("Loading custom JSON codec implementation");
        // TODO: Replace with custom implementation
        return new io.vertx.core.json.jackson.DatabindCodec();
    }

    int order() {
        return 0;
    }

    // TODO: User JsonMapper
    private static class DatabindCodec implements JsonCodec {

        @Override
        public <T> T fromString(String s, Class<T> aClass) throws DecodeException {
            return null;
        }

        @Override
        public <T> T fromBuffer(Buffer buffer, Class<T> aClass) throws DecodeException {
            return null;
        }

        @Override
        public <T> T fromValue(Object o, Class<T> aClass) {
            return null;
        }

        @Override
        public String toString(Object o, boolean b) throws EncodeException {
            return null;
        }

        @Override
        public Buffer toBuffer(Object o, boolean b) throws EncodeException {
            return null;
        }
    }
}
