package ss.agrolavka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ss.agrolavka.constants.VATSCallCommand;
import ss.agrolavka.wrapper.ContactInfo;

import java.util.Map;

@Service
public class VATSService {

    @Value("${vats.secret.incoming:1111")
    private String secretIncoming;

    private static final Logger LOG = LoggerFactory.getLogger(VATSService.class);

    private static final String COMMAND_KEY = "cmd";

    private static final String TOKEN_KEY = "crm_token";

    public Object handleIncomingRequest(Map<String, String> request) {
        if (!secretIncoming.equals(request.get(TOKEN_KEY))) {
            throw new RuntimeException("Access denied for VATS token: " + request.get(TOKEN_KEY));
        }
        if (request.containsKey(COMMAND_KEY)) {
            final var command = VATSCallCommand.valueOf(request.get(COMMAND_KEY));
            if (command == VATSCallCommand.contact) {

            } else {
                return null;
            }
        }
        LOG.warn("Unknown command from VATS");
        return null;
    }

    private ContactInfo findContact(String phone) {

    }
}
