package ss.martin.telegram.bot.api;

import ss.martin.telegram.bot.http.TelegramHttpClient;
import ss.martin.telegram.bot.model.User;

/**
 * Telegram bot.
 * @author alex
 */
public class TelegramBot {
    
    private static final String TELEGRAM_BOT_API = "https://api.telegram.org/bot%s";
    
    private final TelegramHttpClient httpClient;
    
    public TelegramBot(final String token) {
        this.httpClient = new TelegramHttpClient(String.format(TELEGRAM_BOT_API, token));
    }
    
//    private String getUpdates() {
//        return this.httpClient.get("/getUpdates", Update.class);
//    }
    
    public User getMe() {
        return this.httpClient.get("/getMe", User.class);
    }
    
}
