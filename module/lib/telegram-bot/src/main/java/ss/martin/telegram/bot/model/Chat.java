package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Chat.
 * @author alex
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Chat(
    Long id,
    String type,
    String first_name,
    String last_name,
    String username
) {}
