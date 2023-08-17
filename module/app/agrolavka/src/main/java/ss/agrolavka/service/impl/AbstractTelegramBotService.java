package ss.agrolavka.service.impl;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ss.agrolavka.AgrolavkaConfiguration;
import ss.entity.agrolavka.TelegramUser;
import ss.martin.core.dao.CoreDao;
import ss.martin.telegram.bot.api.ReplyMarkupModel;
import ss.martin.telegram.bot.api.TelegramBot;
import ss.martin.telegram.bot.model.Chat;
import ss.martin.telegram.bot.model.Message;
import ss.martin.telegram.bot.model.SendMessage;
import ss.martin.telegram.bot.model.Update;

/**
 * Telegram bot service
 * @author alex
 */
public abstract class AbstractTelegramBotService {
    
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTelegramBotService.class);
    
    protected static final int MESSAGE_TEXT_LIMIT = 4096;
    
    @Autowired
    protected AgrolavkaConfiguration agrolavkaConfiguration;
    
    @Autowired
    protected CoreDao coreDao;
    
    private Set<TelegramUser> botUsers;
    
    protected abstract TelegramBot getTelegramBot();
    
    protected void handleExternalUpdates(final List<Update> updates) {
    }
    
    @PostConstruct
    private void init() {
        LOG.debug("Whitelisted Telegram users: " + agrolavkaConfiguration.telegramUsers());
        final var telegramBot = getTelegramBot();
        final var botName = telegramBot.getBotName();
        this.botUsers = coreDao.getAll(TelegramUser.class).stream()
            .filter(u -> u.getBotName().equals(botName))
            .collect(Collectors.toSet());
        telegramBot.listenUpdates(
            (updates) -> handleUpdates(updates), 
            TimeUnit.SECONDS.toMillis(3), 
            (e) -> LOG.error("Get updates for Telegram bot [" + telegramBot.getBotName() + "] - fail!", e)
        );
        LOG.info("Telegram bot [" + botName + "] started for " + botUsers + " users");
    }
    
    protected List<Message> sendTelegramMessage(final String text, final ReplyMarkupModel keyboard) {
        final var telegramBot = getTelegramBot();
        return coreDao.getAll(TelegramUser.class).stream()
            .filter(user -> user.getBotName().equals(telegramBot.getBotName())).map(user -> {
                final var message = new SendMessage(
                    user.getChatId(), 
                    text, 
                    SendMessage.ParseMode.HTML,
                    keyboard
                );
                return telegramBot.sendMessage(message);
        }).collect(Collectors.toList());
    }
    
    private void handleUpdates(final List<Update> updates) {
        handleUserRegistration(updates);
        handleExternalUpdates(updates);
    }
    
    private void handleUserRegistration(final List<Update> updates) {
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
            final var existUsernames = botUsers.stream().map(u -> u.getUsername()).collect(Collectors.toSet());
            whiteListedUsernames.stream().filter(u -> !existUsernames.contains(u)).forEach(username -> {
                final var user = new TelegramUser();
                user.setUsername(username);
                user.setChatId(chatsMap.get(username).id());
                user.setBotName(botName);
                coreDao.create(user);
                botUsers.add(user);
                LOG.info("New Telegram user has been registered [" + username + "] for bot [" + botName + "]");
            });
        }
    }
}
