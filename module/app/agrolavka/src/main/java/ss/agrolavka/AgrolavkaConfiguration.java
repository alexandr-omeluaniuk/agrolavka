package ss.agrolavka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
    String telegramBotOrders
) {}
