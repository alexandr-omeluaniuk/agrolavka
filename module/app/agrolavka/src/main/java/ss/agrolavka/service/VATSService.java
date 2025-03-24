package ss.agrolavka.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ss.agrolavka.constants.VATSCallCommand;
import ss.agrolavka.dao.OrderDAO;
import ss.agrolavka.wrapper.ContactInfo;
import ss.agrolavka.wrapper.vats.OutgoingCall;
import ss.agrolavka.wrapper.vats.OutgoingVatsCall;
import ss.entity.agrolavka.Order;

import java.util.HashMap;
import java.util.Map;

@Service
public class VATSService {

    private static final Logger LOG = LoggerFactory.getLogger(VATSService.class);

    private static final String COMMAND_KEY = "cmd";

    private static final String TOKEN_KEY = "crm_token";

    private static final String PHONE_KEY = "phone";

    @Value("${vats.secret.incoming:1111}")
    private String secretIncoming;

    @Value("${vats.secret.outgoing:1111}")
    private String secretOutgoing;

    @Value("${vats.address:1111}")
    private String vatsAddress;

    @Value("${vats.mapping:{}")
    private String vatsUsers;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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

    public String phoneApiCall(String request) throws Exception {
        TypeReference<HashMap<String,String>> typeRef = new TypeReference<>() {};
        final var mapping = objectMapper.readValue(vatsUsers, typeRef);
        OutgoingCall call = objectMapper.readValue(request, OutgoingCall.class);
        LOG.info("PHONE API: " + request);
        OutgoingVatsCall payload = new OutgoingVatsCall();
        payload.setClid(call.getSrcNumber());
        payload.setPhone(call.getDestNumber().replaceAll("[^\\d.]", ""));
        payload.setUser(mapping.get(call.getUid()));
        payload.setShow_phone(true);
        final var headers = new HttpHeaders();
        headers.add("X-API-KEY", secretOutgoing);
        final var entity = new HttpEntity(payload, headers);
        final var response = restTemplate.postForEntity(
                vatsAddress,
                entity,
                String.class
        );
        LOG.info("VATS RESPONSE: " + response.getBody());
        return response.getBody();
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
