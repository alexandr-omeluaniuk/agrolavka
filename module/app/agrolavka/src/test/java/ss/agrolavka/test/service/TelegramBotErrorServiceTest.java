package ss.agrolavka.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import ss.agrolavka.service.TelegramBotErrorService;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import ss.entity.agrolavka.TelegramUser;
import ss.martin.base.exception.PlatformException;

public class TelegramBotErrorServiceTest extends AbstractAgrolavkaMvcTest {
    
    @Autowired
    private TelegramBotErrorService service;
    
    @BeforeEach
    protected void before() {
        final var telegramUser = new TelegramUser();
        telegramUser.setBotName("agrolavka_errors_dev_bot");
        telegramUser.setUsername("StarshiStrelok");
        telegramUser.setChatId(5288729591L);
        coreDao.create(telegramUser);
        when(telegramBotErrors.getBotName()).thenReturn("agrolavka_errors_dev_bot");
    }
    
    @Test
    public void testSendAlert() {
        Assertions.assertDoesNotThrow(() -> service.sendAlert(new PlatformException("Test exception")));
        verify(telegramBotErrors, atLeast(1)).sendMessage(any());
    }
}
