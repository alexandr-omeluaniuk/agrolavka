package ss.agrolavka.test.service;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import ss.agrolavka.service.impl.TelegramBotOrderService;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import static ss.agrolavka.test.common.AgrolavkaDataFactory.*;
import ss.entity.agrolavka.EuropostLocationSnapshot;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.TelegramUser;
import ss.martin.security.test.DataFactory;
import ss.martin.telegram.bot.model.Chat;
import ss.martin.telegram.bot.model.Message;

public class TelegramBotOrderServiceTest extends AbstractAgrolavkaMvcTest {
    
    @Autowired
    private TelegramBotOrderService service;
    
    @BeforeEach
    @Override
    protected void before() {
        DataFactory.silentAuthentication(coreDao);
        final var telegramUser = new TelegramUser();
        telegramUser.setBotName("agrolavkadev_bot");
        telegramUser.setUsername("StarshiStrelok");
        telegramUser.setChatId(5288729591L);
        coreDao.create(telegramUser);
        when(telegramBotOrders.getBotName()).thenReturn("agrolavkadev_bot");
        when(telegramBotOrders.sendMessage(any())).thenReturn(
            new Message(
                20L,
                null,
                new Chat(345L, null, null),
                null,
                "SOme teXt"
            )
        );
    }
    
    @Test
    public void testSendNewOrderNotification_Address() {
        final var order = createOrder("1");
        order.setAddress(generateAddress());
        Assertions.assertDoesNotThrow(() -> service.sendNewOrderNotification(order));
        verify(telegramBotOrders, atLeast(1)).sendMessage(any());
    }
    
    @Test
    public void testSendNewOrderNotification_OneClick() {
        final var order = createOrder("2");
        order.setOneClick(true);
        Assertions.assertDoesNotThrow(() -> service.sendNewOrderNotification(order));
        verify(telegramBotOrders, atLeast(1)).sendMessage(any());
    }
    
    @Test
    public void testSendNewOrderNotification_Europost() {
        final var order = createOrder("3");
        final var europostLocation = new EuropostLocationSnapshot();
        europostLocation.setAddress("Кричев, ул.Строителей 12");
        europostLocation.setFirstname("Adam");
        europostLocation.setLastname("Smith");
        europostLocation.setMiddlename("Frank");
        europostLocation.setCity("Moscow");
        europostLocation.setExternalId(UUID.randomUUID().toString());
        order.setEuropostLocationSnapshot(europostLocation);
        Assertions.assertDoesNotThrow(() -> service.sendNewOrderNotification(order));
        verify(telegramBotOrders, atLeast(1)).sendMessage(any());
    }
    
    private Order createOrder(final String postfix) {
        final var order = generateOrder();
        final var product1 = coreDao.create(generateProduct(null, "Семена тыквы " + postfix, 20d, 2d));
        order.getPositions().add(generateOrderPosition(product1));
        final var product2 = coreDao.create(generateProduct(null, "Семена льна " + postfix, 35.20d, 1d));
        order.getPositions().add(generateOrderPosition(product2));
        final var product3 = coreDao.create(generateProduct(null, "Комбикорм супер сила природы " + postfix, 1000d, 2d));
        order.getPositions().add(generateOrderPosition(product3));
        order.getPositions().stream().forEach(pos -> pos.setOrder(order));
        return coreDao.create(order);
    }
}
