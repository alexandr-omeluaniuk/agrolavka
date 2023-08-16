package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Update model.
 * @author alex
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Update(
    @JsonProperty("update_id")
    Long updateId,
    Message message,
    @JsonProperty("callback_query")
    CallbackQuery callbackQuery
) {}
