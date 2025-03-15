package ss.agrolavka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VATSService {

    private static final Logger LOG = LoggerFactory.getLogger(VATSService.class);

    public void handleIncomingRequest(Map<String, String> request) {
        LOG.info("VATS: ");
        request.entrySet().forEach(entry -> {
            LOG.info(entry.getKey() + " = " + entry.getValue());
        });
    }
}
