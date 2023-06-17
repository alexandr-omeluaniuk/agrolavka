package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Send message.
 * @author alex
 */
public record SendMessage(
    @JsonProperty("chat_id")
    Long chatId,
    String text,
    @JsonProperty("parse_mode")
    ParseMode parseMode
) {
    
    /**
     * Parse mode.
     */
    public enum ParseMode {
        Markdown, MarkdownV2, HTML
    }
}
