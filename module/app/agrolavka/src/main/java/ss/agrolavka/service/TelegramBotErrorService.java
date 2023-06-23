package ss.agrolavka.service;

import java.io.PrintWriter;
import java.io.StringWriter;
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
    
    private static final String ERROR_ALERT_TEMPLATE = """
<code>%s</code>
""";
    
    @Autowired
    @Qualifier("telegramBotErrors")
    private TelegramBot telegramBot;
    
    public void sendAlert(final Exception exception) {
        sendHtml(getMessageText(exception));
    }
    
    private String getMessageText(final Exception exception) {
        var text = String.format(ERROR_ALERT_TEMPLATE, exception.getMessage(), getStacktrace(exception));
        final var limit = MESSAGE_TEXT_LIMIT - ERROR_ALERT_TEMPLATE.length();
        if (text.length() > limit) {
            text = text.substring(0, limit) + "</code>";
        }
        return text;
    }
    
    private String getStacktrace(final Exception exception) {
        final var sw = new StringWriter();
        final var pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }

    @Override
    protected TelegramBot getTelegramBot() {
        return telegramBot;
    }
}
