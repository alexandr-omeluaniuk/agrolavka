package ss.martin.telegram.bot.test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ss.martin.telegram.bot.api.TelegramBot;
import ss.martin.telegram.bot.model.SendMessage;

public class TelegramBotTest {
    
    private final TelegramBot bot = new TelegramBot("6228786851:AAEGxRSAF9EcvgZycosFxrG0CWjfSGw4xhY");
    
    private static final long CHAT_ID = 5288729591L;
    
    @Test
    public void testGetMe() {
        final var user = bot.getMe();
        assertNotNull(user);
        assertEquals("AgrolavkaTestBot", user.username());
    }
    
    @Test
    public void testSendMessage() {
        final var message = new SendMessage(
            CHAT_ID, 
            "Hello Alex!!\n<a href=\"http://www.example.com/\">inline URL</a>", 
            SendMessage.ParseMode.HTML
        );
        final var response = bot.sendMessage(message);
        System.out.println(response);
    }
    
    @Test
    public void testListenUpdates() throws InterruptedException {
        final var exceptionHandler = new ExceptionHandler();
        bot.listenUpdates((updates) -> {
            assertFalse(updates.isEmpty());
            System.out.println(updates);
        }, TimeUnit.SECONDS.toMillis(1), exceptionHandler);
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        Optional.ofNullable(exceptionHandler.getError()).ifPresent((e) -> fail("Telegram bot exception!", e));
    }
    
    private class ExceptionHandler implements Consumer<Exception> {
        
        private Exception e;

        @Override
        public void accept(final Exception e) {
            this.e = e;
        }
        
        public Exception getError() {
            return e;
        }
    }
}
