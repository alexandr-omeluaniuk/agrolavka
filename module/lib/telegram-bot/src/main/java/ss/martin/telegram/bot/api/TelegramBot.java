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
    
    private String botName;
    
    public TelegramBot(final String token) {
        this.httpClient = new TelegramHttpClient(String.format(TELEGRAM_BOT_API, token));
    }
    
    /**
     * Listen bot updates.
     * @param updatesConsumer updates consumer.
     * @param interval interval for requests.
     * @param errorHandler error handler.
     */
    public synchronized void listenUpdates(
        final Consumer<List<Update>> updatesConsumer, 
        final long interval,
        final Consumer<Exception> errorHandler
    ) {
        if (updatesListener == null) {
            updatesListener = new Thread(new UpdatesThread(updatesConsumer, interval, errorHandler));
            this.botName = getMe().username();
            updatesListener.setName("telegram-bot-" + botName.toLowerCase());
            updatesListener.start();
        }
    }
    
    public String getBotName() {
        return this.botName;
    }
    
    public String sendMessage(final SendMessage message) {
        return this.httpClient.post("/sendMessage", message);
    }
    
    private List<Update> getUpdates(final long offset) {
        return this.httpClient.getList("/getUpdates?offset=" + offset, Update.class);
    }
    
    private User getMe() {
        return this.httpClient.get("/getMe", User.class);
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
                        offset = upd.updateId() + 1;
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
