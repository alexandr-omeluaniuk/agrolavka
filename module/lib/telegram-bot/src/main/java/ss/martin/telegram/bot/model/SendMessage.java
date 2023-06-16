package ss.martin.telegram.bot.model;

/**
 * Send message.
 * @author alex
 */
public record SendMessage(
    Long chat_id,
    String text,
    ParseMode parse_mode
) {
    public enum ParseMode {
        Markdown, MarkdownV2, HTML
    }
}
