package ss.agrolavka.test.common;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ss.martin.security.test.PlatformSecurityMvcTest;
import ss.martin.telegram.bot.api.TelegramBot;

@SpringBootTest(classes = AgrolavkaTestApplication.class)
public abstract class AbstractAgrolavkaMvcTest extends PlatformSecurityMvcTest {
    
    @MockBean(name = "telegramBotOrders")
    protected TelegramBot telegramBotOrders;
    
    @MockBean(name = "telegramBotErrors")
    protected TelegramBot telegramBotErrors;
}
