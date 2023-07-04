package ss.agrolavka.service;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ss.martin.base.constants.PlatformConfiguration;
import ss.martin.security.api.AlertService;
import ss.martin.telegram.bot.api.TelegramBot;

/**
 * Service for sending alerts via Telegram
 * @author alex
 */
@Service
public class TelegramBotErrorService extends AbstractTelegramBotService implements AlertService {
    
    private static final String ERROR_ALERT_TEMPLATE = """
%s

%s
""";
    
    @Autowired
    @Qualifier("telegramBotErrors")
    private TelegramBot telegramBot;
    
    @Override
    public void sendAlert(final String message, final Exception exception) {
        final var title = Optional.ofNullable(message).orElse("An unknown Agrolavka error");
        final var alertMessage = String.format(ERROR_ALERT_TEMPLATE, title, getExceptionText(exception, title));
        sendHtml(alertMessage);
    }
    
    private String getExceptionText(final Exception exception, final String message) {
        if (exception == null) {
            return "";
        }
        final var startTag = "<code>";
        final var endTag = "</code>";
        var stacktrace = getStacktrace(exception);
        final var limit = MESSAGE_TEXT_LIMIT - startTag.length() - endTag.length() - message.length() 
            - ERROR_ALERT_TEMPLATE.length();
        if (stacktrace.length() > limit) {
            stacktrace = stacktrace.substring(0, limit);
        }
        return startTag + stacktrace + endTag;
    }
    
    private String getStacktrace(final Exception exception) {
        final var sb = new StringBuilder();
        Throwable e = exception;
        while (e != null) {
            sb.append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
            sb.append(getPlatformStacktrace(e)).append("\n");
            e = e.getCause();
        }
        return sb.toString();
    }
    
    private String getPlatformStacktrace(final Throwable th) {
        final var sb = new StringBuilder();
        Stream.of(th.getStackTrace())
            .filter(line -> line.getClassName().startsWith(PlatformConfiguration.BASE_PACKAGE_SCAN))
            .forEach(line -> {
                sb.append(line.toString().replace("<", "&lt;").replace("<", "&gt;")).append("\n");
            });
        return sb.toString();
    }

    @Override
    protected TelegramBot getTelegramBot() {
        return telegramBot;
    }
}
