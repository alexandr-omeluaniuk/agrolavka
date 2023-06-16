package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ss.martin.telegram.bot.util.RawJsonDeserializer;

/**
 * Response.
 * @author alex
 */
public record Response(
    boolean ok,
    @JsonDeserialize(using = RawJsonDeserializer.class)
    String result,
    String error_code,
    String description
) {}
