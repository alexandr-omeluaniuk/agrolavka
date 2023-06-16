package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Message(
    @JsonProperty("message_id")
    Long messageId,
    User from,
    Chat chat,
    Long date,
    String text
) {}
