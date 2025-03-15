package ss.agrolavka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.VATSCallCommand;
import ss.agrolavka.dao.OrderDAO;
import ss.agrolavka.wrapper.ContactInfo;
import ss.entity.agrolavka.Order;

import java.util.Map;

@Service
public class VATSService {

    private static final Logger LOG = LoggerFactory.getLogger(VATSService.class);

    private static final String COMMAND_KEY = "cmd";

    private static final String TOKEN_KEY = "crm_token";

    private static final String PHONE_KEY = "phone";

    @Value("${vats.secret.incoming:1111}")
    private String secretIncoming;

    @Autowired
    private OrderDAO orderDAO;

    public Object handleIncomingRequest(Map<String, String> request) {
        if (!secretIncoming.equals(request.get(TOKEN_KEY))) {
            throw new RuntimeException("Access denied for VATS token: " + request.get(TOKEN_KEY));
        }
        if (request.containsKey(COMMAND_KEY)) {
            final var command = VATSCallCommand.valueOf(request.get(COMMAND_KEY));
            if (command == VATSCallCommand.contact) {
                return findContact(request.get(PHONE_KEY));
            } else {
                return null;
            }
        }
        LOG.warn("Unknown command from VATS");
        return null;
    }

    public void phoneApiCall(String request) {
        LOG.info("PHONE API: " + request);
    }

    private ContactInfo findContact(String phone) {
        final var orders = orderDAO.getPurchaseHistoryByPhone(phone);
        if (orders.isEmpty()) {
            final var agents = orderDAO.getAgentsByPhone(phone);
            if (agents.isEmpty()) {
                return null;
            } else {
                final var info = new ContactInfo();
                info.setContact_name("КА " + agents.get(0).getName());
                LOG.info("VATS agent: " + info.getContact_name());
                return info;
            }
        } else {
            final var sb = new StringBuilder("Клиент ");
            boolean isFound = false;
            for (Order order: orders) {
                if (order.getAddress() != null) {
                    if (order.getAddress().getFirstname() != null) {
                        sb.append(order.getAddress().getFirstname()).append(" ");
                    }
                    if (order.getAddress().getLastname() != null) {
                        sb.append(order.getAddress().getLastname());
                    }
                    isFound = true;
                    break;
                } else if (order.getEuropostLocationSnapshot() != null) {
                    if (order.getEuropostLocationSnapshot().getFirstname() != null) {
                        sb.append(order.getEuropostLocationSnapshot().getFirstname()).append(" ");
                    }
                    if (order.getEuropostLocationSnapshot().getLastname() != null) {
                        sb.append(order.getEuropostLocationSnapshot().getLastname());
                    }
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                sb.append("Без имени");
            }
//            final var lastOrder = orders.get(0);
//            sb.append(", заказ #").append(lastOrder.getId());
//            if (orders.size() > 1) {
//                sb.append(", заказов [").append(orders.size()).append("]");
//            }
            final var info = new ContactInfo();
            info.setContact_name(sb.toString());
            // info.setResponsible("admin");
            LOG.info("VATS client: " + info.getContact_name());
            return info;
        }
    }
}
