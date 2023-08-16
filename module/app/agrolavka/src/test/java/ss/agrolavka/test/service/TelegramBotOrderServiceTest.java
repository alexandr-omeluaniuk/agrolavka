package ss.agrolavka.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import ss.agrolavka.service.impl.TelegramBotOrderService;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import static ss.agrolavka.test.common.AgrolavkaDataFactory.*;
import ss.entity.agrolavka.EuropostLocationSnapshot;
import ss.entity.agrolavka.TelegramUser;

public class TelegramBotOrderServiceTest extends AbstractAgrolavkaMvcTest {
    
    @Autowired
    private TelegramBotOrderService service;
    
    @BeforeEach
    @Override
    protected void before() {
        final var telegramUser = new TelegramUser();
        telegramUser.setBotName("agrolavkadev_bot");
        telegramUser.setUsername("StarshiStrelok");
        telegramUser.setChatId(5288729591L);
        coreDao.create(telegramUser);
        when(telegramBotOrders.getBotName()).thenReturn("agrolavkadev_bot");
    }
    
    @Test
    public void testSendNewOrderNotification_Address() {
        final var order = generateOrder();
        order.setAddress(generateAddress());
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Семена тыквы", 20d, 2d)));
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Семена льна", 35.20d, 1d)));
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Комбикорм супер сила природы", 1000d, 2d)));
        order.setId(25L);
        Assertions.assertDoesNotThrow(() -> service.sendNewOrderNotification(order, 100d));
        verify(telegramBotOrders, atLeast(1)).sendMessage(any());
    }
    
    @Test
    public void testSendNewOrderNotification_OneClick() {
        final var order = generateOrder();
        order.setOneClick(true);
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Семена тыквы", 20d, 2d)));
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Семена льна", 35.20d, 1d)));
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Комбикорм супер сила природы", 1000d, 2d)));
        order.setId(25L);
        Assertions.assertDoesNotThrow(() -> service.sendNewOrderNotification(order, 100d));
        verify(telegramBotOrders, atLeast(1)).sendMessage(any());
    }
    
    @Test
    public void testSendNewOrderNotification_Europost() {
        final var order = generateOrder();
        final var europostLocation = new EuropostLocationSnapshot();
        europostLocation.setAddress("Кричев, ул.Строителей 12");
        europostLocation.setFirstname("Adam");
        europostLocation.setLastname("Smith");
        europostLocation.setMiddlename("Frank");
        order.setEuropostLocationSnapshot(europostLocation);
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Семена тыквы", 20d, 2d)));
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Семена льна", 35.20d, 1d)));
        order.getPositions().add(generateOrderPosition(generateProduct(null, "Комбикорм супер сила природы", 1000d, 2d)));
        order.setId(25L);
        Assertions.assertDoesNotThrow(() -> service.sendNewOrderNotification(order, 100d));
        verify(telegramBotOrders, atLeast(1)).sendMessage(any());
    }
}
