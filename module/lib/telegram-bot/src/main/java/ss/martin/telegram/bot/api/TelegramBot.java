package ss.martin.telegram.bot.api;

import java.util.List;
import java.util.function.Consumer;
import ss.martin.telegram.bot.http.TelegramHttpClient;
import ss.martin.telegram.bot.model.SendMessage;
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
        if (updatesListener == null) {
            updatesListener = new Thread(new UpdatesThread(updatesConsumer, interval, errorHandler));
            updatesListener.setName("telegram-bot-" + getMe().username().toLowerCase());
            updatesListener.start();
        }
    }
    
    public User getMe() {
        return this.httpClient.get("/getMe", User.class);
    }
    
    public String sendMessage(final SendMessage message) {
        return this.httpClient.post("/sendMessage", message);
    }
    
    private List<Update> getUpdates(final long offset) {
        return this.httpClient.getList("/getUpdates?offset=" + offset, Update.class);
    }
    
    private class UpdatesThread implements Runnable {
        
        private final Consumer<List<Update>> updatesConsumer;
        private final long interval;
        private final Consumer<Exception> errorHandler;
        
        private long offset = 0;
        
        public UpdatesThread(
            final Consumer<List<Update>> updatesConsumer, 
            final long interval, 
            final Consumer<Exception> errorHandler
        ) {
            this.updatesConsumer = updatesConsumer;
            this.interval = interval;
            this.errorHandler = errorHandler;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    final var updates = getUpdates(offset);
                    updates.stream().reduce((first, second) -> second).ifPresent(upd -> {
                        offset = upd.update_id() + 1;
                    });
                    updatesConsumer.accept(updates);
                    Thread.sleep(interval);
                } catch (Exception e) {
                    errorHandler.accept(e);
                }
            }
        }
    }
}
