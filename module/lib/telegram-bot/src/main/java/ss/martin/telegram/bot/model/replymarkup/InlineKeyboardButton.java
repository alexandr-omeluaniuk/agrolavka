package ss.martin.telegram.bot.model.replymarkup;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * InlineKeyboardButton
 * @author alex
 */
public record InlineKeyboardButton(
    String text,
    @JsonProperty("callback_data")
    String callbackData
) {}
