package ss.martin.telegram.bot.api;

import java.util.List;
import java.util.function.Consumer;
import ss.martin.telegram.bot.http.TelegramHttpClient;
import ss.martin.telegram.bot.model.EditMessageReplyMarkup;
import ss.martin.telegram.bot.model.EditMessageText;
import ss.martin.telegram.bot.model.Message;
import ss.martin.telegram.bot.model.SendMessage;
import ss.martin.telegram.bot.model.Update;
import ss.martin.telegram.bot.model.User;

/**
 * Telegram bot.
 * @author alex
 */
public class TelegramBot {
    
    public static final String TELEGRAM_HOST = "https://api.telegram.org";
    
    private static final String TELEGRAM_BOT_API = "/bot%s";
    
    private final TelegramHttpClient httpClient;
    
    private Thread updatesListener;
    
    private String botName;
    
    public TelegramBot(final String token) {
        this(token, TELEGRAM_HOST);
    }
    
    public TelegramBot(final String token, final String telegramHost) {
        this.httpClient = new TelegramHttpClient(String.format(telegramHost + TELEGRAM_BOT_API, token));
        this.botName = getMe().username();
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
            updatesListener.setName("telegram-bot-" + botName.toLowerCase());
            updatesListener.start();
        }
    }
    
    public String getBotName() {
        return this.botName;
    }
    
    public Message sendMessage(final SendMessage message) {
        return this.httpClient.post("/sendMessage", message, Message.class);
    }
    
    public Message updateMessageText(final EditMessageText updates) {
        return this.httpClient.post("/editMessageText", updates, Message.class);
    }
    
    public Message updateMessageReplyMarkup(final EditMessageReplyMarkup updates) {
        return this.httpClient.post("/editMessageReplyMarkup", updates, Message.class);
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
                } catch (Exception e) {
                    errorHandler.accept(e);
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException ex) {
                    errorHandler.accept(ex);
                }
            }
        }
    }
}
