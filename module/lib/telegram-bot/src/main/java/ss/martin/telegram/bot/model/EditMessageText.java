package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ss.martin.telegram.bot.api.ReplyMarkupModel;

/**
 * Edit message text
 * @author alex
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EditMessageText(
    @JsonProperty("message_id")
    Long messageId,
    @JsonProperty("chat_id")
    Long chatId,
    String text,
    @JsonProperty("parse_mode")
    SendMessage.ParseMode parseMode,
    @JsonProperty("reply_markup")
    ReplyMarkupModel replyMarkup
) {}
