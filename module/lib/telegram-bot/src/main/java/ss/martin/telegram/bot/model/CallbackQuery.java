package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Callback query.
 * @author alex
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CallbackQuery(
    Message message,
    String data
) {}
