package ss.agrolavka.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ss.entity.agrolavka.Order;
import ss.martin.security.configuration.external.DomainConfiguration;
import ss.martin.telegram.bot.api.TelegramBot;
import ss.martin.telegram.bot.formatter.TableFormatter;
import static ss.martin.telegram.bot.formatter.TableFormatter.*;
import ss.martin.telegram.bot.model.CreatedMessage;
import ss.martin.telegram.bot.model.replymarkup.InlineKeyboardButton;
import ss.martin.telegram.bot.model.replymarkup.InlineKeyboardMarkup;

/**
 * Telegram bots service.
 * @author alex
 */
@Service
public class TelegramBotOrderService extends AbstractTelegramBotService {
    
    private static final String ORDER_TEMPLATE = """
Поступил новый заказ <a href="%s">%s</a>,
%s
<pre>%s</pre>
%s
""";
    
    @Autowired
    @Qualifier("telegramBotOrders")
    private TelegramBot telegramBot;
    
    @Autowired
    private DomainConfiguration domainConfiguration;
    
    public void sendNewOrderNotification(final Order order, final Double total) {
        final var keyboard = new InlineKeyboardMarkup(Arrays.asList(
            Arrays.asList(
                new InlineKeyboardButton(
                    "One", "1"
                )
            )
        ));
        final var messages = sendHtml(getOrderMessage(order, total), keyboard);
        order.setTelegramMessages(messages.stream().map(CreatedMessage::messageId)
            .collect(Collectors.toList()).toArray(Long[]::new));
        coreDao.update(order);
    }
    
    private String getOrderMessage(final Order order, final Double total) {
        final var link = domainConfiguration.host() + "/admin/app/agrolavka/order/" + order.getId();
        return String.format(
            ORDER_TEMPLATE, 
            link,
            order.getId(),
            getContactInfo(order),
            new TableFormatter(createTable(order, total)).format(),
            getDeliveryInfo(order)
        );
    }
    
    private String getContactInfo(final Order order) {
        final var sb = new StringBuilder();
        Optional.ofNullable(order.getAddress()).ifPresent(address -> {
            Optional.ofNullable(address.getLastname()).ifPresent(v -> sb.append(v).append(" "));
            Optional.ofNullable(address.getFirstname()).ifPresent(v -> sb.append(v).append(" "));
            Optional.ofNullable(address.getMiddlename()).ifPresent(v -> sb.append(v));
        });
        Optional.ofNullable(order.getEuropostLocationSnapshot()).ifPresent(europost -> {
            Optional.ofNullable(europost.getLastname()).ifPresent(v -> sb.append(v).append(" "));
            Optional.ofNullable(europost.getFirstname()).ifPresent(v -> sb.append(v).append(" "));
            Optional.ofNullable(europost.getMiddlename()).ifPresent(v -> sb.append(v));
        });
        sb.append("\n");
        sb.append("<a href=\"https://t.me/+375").append(order.getPhone().replaceAll("[^\\d.]", "")).append("\">");
        sb.append(order.getPhone());
        sb.append("</a>");
        return sb.toString();
    }
    
    private String getDeliveryInfo(final Order order) {
        final var sb = new StringBuilder();
        sb.append("<i>");
        final var address = order.getAddress();
        if (address != null) {
            sb.append("<b>Почта:</b>\n");
            Optional.ofNullable(address.getRegion()).ifPresent(v -> sb.append("Область: ").append(v).append("\n"));
            Optional.ofNullable(address.getDistrict()).ifPresent(v -> sb.append("Район: ").append(v).append("\n"));
            Optional.ofNullable(address.getCity()).ifPresent(v -> sb.append("Населенный пункт: ").append(v).append("\n"));
            Optional.ofNullable(address.getStreet()).ifPresent(v -> sb.append("Улица: ").append(v).append("\n"));
            Optional.ofNullable(address.getHouse()).ifPresent(v -> sb.append("Дом: ").append(v).append("\n"));
            Optional.ofNullable(address.getFlat()).ifPresent(v -> sb.append("Квартира: ").append(v).append("\n"));
            Optional.ofNullable(address.getPostcode()).ifPresent(v -> sb.append("Почтовый индекс: ").append(v).append("\n"));
        } else if (order.getEuropostLocationSnapshot() != null) {
            sb.append("<b>Европочта:</b> ").append(order.getEuropostLocationSnapshot().getAddress());
        } else {
            sb.append("<b>Самовывоз</b>");
        }
        sb.append("</i>");
        if (Boolean.TRUE.equals(order.getOneClick())) {
            sb.append("\n").append("<i>Заказ в один клик</i>");
        }
        Optional.ofNullable(order.getComment()).ifPresent(comment -> {
            if (!comment.isBlank()) {
                sb.append("\n\n").append("<i><b>Комментарий</b></i>: ").append(comment);
            }
        });
        return sb.toString();
    }
    
    private Table createTable(final Order order, final Double total) {
        final var positions = order.getPositions();
        Collections.sort(positions);
        final var rows = new Row[positions.size() + 1];
        final var header = new Header(new HeaderCell[]{
            new HeaderCell("Наименование", Align.LEFT, HeaderCell.WIDTH_AUTO),
            new HeaderCell("Кол", Align.RIGHT), 
            new HeaderCell("Сумма", Align.RIGHT)
        });
        for (int i = 0; i < positions.size(); i++) {
            final var position = positions.get(i);
            rows[i] = new Row(
                new Cell[] {
                    new Cell(position.getProductName()),
                    new Cell(position.getQuantity().toString()),
                    new Cell(String.format("%.2f", position.getPrice() * position.getQuantity()))
                }
            );
        }
        rows[rows.length - 1] = new Row(
            new Cell[] {
                new Cell("Сумма"),
                new Cell(null),
                new Cell(String.format("%.2f", total))
            }
        );
        return new Table(header, rows, 30, 1, "");
    }
    
    @Override
    protected TelegramBot getTelegramBot() {
        return telegramBot;
    }
}
