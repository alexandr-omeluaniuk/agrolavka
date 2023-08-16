package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import ss.martin.telegram.bot.api.ReplyMarkupModel;

/**
 * Send message.
 * @author alex
 */
@JsonInclude(Include.NON_NULL)
public record SendMessage(
    @JsonProperty("chat_id")
    Long chatId,
    String text,
    @JsonProperty("parse_mode")
    ParseMode parseMode,
    @JsonProperty("reply_markup")
    ReplyMarkupModel replyMarkup
) {
    
    public SendMessage(Long chatId, String text, ParseMode parseMode) {
        this(chatId, text, parseMode, null);
    }
    
    /**
     * Parse mode.
     */
    public static enum ParseMode {
        Markdown, MarkdownV2, HTML
    }
    
}
