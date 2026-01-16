package ss.agrolavka;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Site configuration.
 * @author alex
 */
@ConfigurationProperties(prefix = "agrolavka")
public record AgrolavkaConfiguration(
    String mySkladUsername,
    String mySkladPassword,
    String backgroundUserUsername,
    String backgroundUserPassword,
    String telegramBotOrders,
    String telegramBotErrors,
    List<String> telegramUsers,
    List<String> blacklistIp
) {}
