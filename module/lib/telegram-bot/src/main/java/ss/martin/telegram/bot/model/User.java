package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User.
 * @author alex
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
    Long id,
    Boolean is_bot,
    String first_name,
    String last_name,
    String username,
    String language_code,
    Boolean is_premium,
    Boolean added_to_attachment_menu,
    Boolean can_join_groups,
    Boolean can_read_all_group_messages,
    Boolean supports_inline_queries
) {}
