package ss.martin.telegram.bot.api;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import ss.martin.base.lang.ThrowingRunnable;
import ss.martin.telegram.bot.http.TelegramHttpClient;
import ss.martin.telegram.bot.model.Update;
import ss.martin.telegram.bot.model.User;

/**
 * Telegram bot.
 * @author alex
 */
public class TelegramBot {
    
    private static final String TELEGRAM_BOT_API = "https://api.telegram.org/bot%s";
    
    private final TelegramHttpClient httpClient;
    
    private Thread updatesListener;
    
    public TelegramBot(final String token) {
        this.httpClient = new TelegramHttpClient(String.format(TELEGRAM_BOT_API, token));
    }
    
    public synchronized void listenUpdates(
        final Consumer<List<Update>> updatesConsumer, 
        final long interval,
        final Consumer<Exception> errorHandler
    ) {
        Optional.ofNullable(updatesListener).map(thread -> {
            thread.interrupt();
            return newUpdatesListener(updatesConsumer, interval, errorHandler);
        }).orElseGet(() -> newUpdatesListener(updatesConsumer, interval, errorHandler))
            .start();
    }
    
    public User getMe() {
        return this.httpClient.get("/getMe", User.class);
    }
    
    private List<Update> getUpdates() {
        return this.httpClient.getList("/getUpdates", Update.class);
    }
    
    private Thread newUpdatesListener(
        final Consumer<List<Update>> updatesConsumer, 
        final long interval, 
        final Consumer<Exception> errorHandler
    ) {
        return new Thread((ThrowingRunnable) () -> {
            try {
                while (true) {
                    updatesConsumer.accept(getUpdates());
                    Thread.sleep(interval);
                }
            } catch (Exception e) {
                errorHandler.accept(e);
            }
        });
    }
    
}
