package ss.martin.images.storage.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Base64;

/**
 * Byte array deserializer.
 * @author ss
 */
public class ByteArrayDeserializer extends StdDeserializer<byte[]> {
    
    public ByteArrayDeserializer() { 
        this(null); 
    }
    
    public ByteArrayDeserializer(Class<byte[]> t) { 
        super(t); 
    }
    
    @Override
    public byte[] deserialize(
        final JsonParser jp, 
        final DeserializationContext dc
    ) throws IOException, JsonProcessingException {
        try {
            String value = jp.getText();
            if (value.contains(",")) {
                return Base64.getDecoder().decode(value.split(",")[1]);
            } else {
                return Base64.getDecoder().decode(value);
            }
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
