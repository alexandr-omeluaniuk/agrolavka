package ss.agrolavka.service.impl;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ss.agrolavka.AgrolavkaConfiguration;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.TelegramUser;
import ss.martin.core.dao.CoreDao;
import ss.martin.security.configuration.external.DomainConfiguration;
import ss.martin.telegram.bot.api.TelegramBot;
import ss.martin.telegram.bot.model.Chat;
import ss.martin.telegram.bot.model.SendMessage;
import ss.martin.telegram.bot.model.Update;

/**
 * Telegram bots service.
 * @author alex
 */
@Service
public class TelegramBotsService {
    
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotsService.class);
    
    @Autowired
    @Qualifier("telegramBotOrders")
    private TelegramBot telegramBot;
    
    @Autowired
    private CoreDao coreDao;
    
    @Autowired
    private AgrolavkaConfiguration agrolavkaConfiguration;
    
    @Autowired
    private DomainConfiguration domainConfiguration;
    
    @PostConstruct
    void init() {
        LOG.info("Whitelisted Telegram users: " + agrolavkaConfiguration.telegramUsers());
        telegramBot.listenUpdates(
            (updates) -> handleUpdates(updates, telegramBot.getBotName()), 
            TimeUnit.MINUTES.toMillis(1), 
            (e) -> LOG.error("Get updates for Telegram bot - fail!", e)
        );
    }
    
    public void sendNewOrderNotification(final Order order, final Double total) {
        final var textTemplate = """
Поступил новый заказ,
Потенциальная сумма заказа - %s BYN,
Номер заказа: <a href="%s">%s</a>
""";
        final var sum = String.format("%.2f", total);
        final var link = domainConfiguration.host() + "/admin/app/agrolavka/order/" + order.getId();
        final var text = String.format(textTemplate, sum, link, order.getId());
        coreDao.getAll(TelegramUser.class).stream()
            .filter(user -> user.getBotName().equals(telegramBot.getBotName())).forEach(user -> {
                final var message = new SendMessage(user.getChatId(), text, SendMessage.ParseMode.HTML);
                telegramBot.sendMessage(message);
        });
    }
    
    private void handleUpdates(final List<Update> updates, final String botName) {
        final var chatsMap = new HashMap<String, Chat>();
        for (final var update : updates) {
            if (update.message() != null && update.message().from() != null && update.message().chat() != null) {
                chatsMap.put(update.message().from().username(), update.message().chat());
            }
        }
        final var whiteListedUsernames = chatsMap.keySet().stream()
            .filter(u -> agrolavkaConfiguration.telegramUsers().contains(u)).collect(Collectors.toList());
        if (!whiteListedUsernames.isEmpty()) {
            final var existUsernames = coreDao.getAll(TelegramUser.class).stream().map(u -> u.getUsername())
                .collect(Collectors.toList());
            whiteListedUsernames.stream().filter(u -> !existUsernames.contains(u)).forEach(username -> {
                final var user = new TelegramUser();
                user.setUsername(username);
                user.setChatId(chatsMap.get(username).id());
                user.setBotName(botName);
                coreDao.create(user);
                LOG.info("New Telegram user has been registered: " + username);
            });
        }
    }
}