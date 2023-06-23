package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ss.martin.telegram.bot.api.TelegramBot;

/**
 * Service for sending alerts via Telegram
 * @author alex
 */
@Service
public class TelegramBotErrorService extends AbstractTelegramBotService {
    
    @Autowired
    @Qualifier("telegramBotErrors")
    private TelegramBot telegramBot;

    @Override
    protected TelegramBot getTelegramBot() {
        return telegramBot;
    }
}
