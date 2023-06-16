package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Message.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Message(
    Long message_id,
    User from,
    Chat chat,
    Long date,
    String text,
    MessageEntity[] entities
) {}
