package ss.agrolavka;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    List<String> telegramUsers
) {}
