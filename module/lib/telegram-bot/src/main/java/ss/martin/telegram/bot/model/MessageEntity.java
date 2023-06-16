package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Message entity.
 * @author alex
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageEntity(
    int offset,
    int length,
    String type
) {}
