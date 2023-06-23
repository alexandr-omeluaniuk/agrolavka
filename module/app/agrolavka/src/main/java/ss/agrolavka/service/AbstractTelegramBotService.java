package ss.agrolavka.service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ss.agrolavka.AgrolavkaConfiguration;
import ss.entity.agrolavka.TelegramUser;
import ss.martin.core.dao.CoreDao;
import ss.martin.telegram.bot.api.TelegramBot;
import ss.martin.telegram.bot.model.Chat;
import ss.martin.telegram.bot.model.Update;

/**
 * Telegram bot service
 * @author alex
 */
public abstract class AbstractTelegramBotService {
    
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTelegramBotService.class);
    
    @Autowired
    protected AgrolavkaConfiguration agrolavkaConfiguration;
    
    @Autowired
    protected CoreDao coreDao;
    
    protected abstract TelegramBot getTelegramBot();
    
    @PostConstruct
    private void init() {
        final var telegramBot = getTelegramBot();
        LOG.debug("Whitelisted Telegram users: " + agrolavkaConfiguration.telegramUsers());
        telegramBot.listenUpdates(
            (updates) -> handleUpdates(updates), 
            TimeUnit.MINUTES.toMillis(1), 
            (e) -> LOG.error("Get updates for Telegram bot [" + telegramBot.getBotName() + "] - fail!", e)
        );
    }
    
    private void handleUpdates(final List<Update> updates) {
        final var botName = getTelegramBot().getBotName();
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
                LOG.info("New Telegram user has been registered [" + username + "] for bot [" + botName + "]");
            });
        }
    }
}
