package ss.agrolavka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VATSService {

    private static final Logger LOG = LoggerFactory.getLogger(VATSService.class);

    public void handleIncomingRequest(String request) {
        LOG.info("VATS: " + request);
    }
}
