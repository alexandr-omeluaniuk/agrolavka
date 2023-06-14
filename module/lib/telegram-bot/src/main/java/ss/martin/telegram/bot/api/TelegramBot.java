package ss.martin.telegram.bot.api;

import ss.martin.telegram.bot.http.TelegramHttpClient;

/**
 * Telegram bot.
 * @author alex
 */
public class TelegramBot {
    
    private static final String TELEGRAM_BOT_API = "https://api.telegram.org/bot%s";
    
    private final TelegramHttpClient httpClient;
    
    public TelegramBot(final String token) {
        this.httpClient = new TelegramHttpClient(String.format(TELEGRAM_BOT_API, token));
        final var handshakeResponse = this.httpClient.get("/getMe");
        System.out.println(handshakeResponse);
    }
    
}
