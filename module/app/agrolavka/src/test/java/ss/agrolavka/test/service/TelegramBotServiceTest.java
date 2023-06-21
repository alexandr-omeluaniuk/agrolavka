package ss.agrolavka.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.agrolavka.service.impl.TelegramBotsService;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import static ss.agrolavka.test.common.AgrolavkaDataFactory.*;
import ss.entity.agrolavka.TelegramUser;

public class TelegramBotServiceTest extends AbstractAgrolavkaMvcTest {
    
    @Autowired
    private TelegramBotsService service;
    
    @BeforeEach
    protected void beforeAll() {
        final var telegramUser = new TelegramUser();
        telegramUser.setBotName("agrolavkadev_bot");
        telegramUser.setUsername("StarshiStrelok");
        telegramUser.setChatId(5288729591L);
        coreDao.create(telegramUser);
    }
    
    @Test
    public void testSendNewOrderNotification() {
        final var order = generateOrder();
        order.setAddress(generateAddress());
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Семена тыквы", 20d, 2d)));
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Семена льна", 35.20d, 1d)));
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Комбикорм супер сила природы", 1000d, 2d)));
        order.setId(25L);
        service.sendNewOrderNotification(order, 100d);
    }
}
