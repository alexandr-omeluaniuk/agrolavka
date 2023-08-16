package ss.martin.telegram.bot.model.replymarkup;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import ss.martin.telegram.bot.api.ReplyMarkupModel;

/**
 * InlineKeyboardMarkup.
 * @author alex
 */
public record InlineKeyboardMarkup(
    @JsonProperty("inline_keyboard")
    List<List<InlineKeyboardButton>> inlineKeyboard
) implements ReplyMarkupModel {}
