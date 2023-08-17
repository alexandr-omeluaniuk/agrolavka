package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ss.martin.telegram.bot.api.ReplyMarkupModel;

/**
 *
 * @author alex
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EditMessageReplyMarkup(
    @JsonProperty("message_id")
    Long messageId,
    @JsonProperty("chat_id")
    Long chatId,
    @JsonProperty("reply_markup")
    ReplyMarkupModel replyMarkup
) {}
