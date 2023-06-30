package ss.martin.security.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ss.martin.security.api.AlertService;

@Service
class SimpleAlertService implements AlertService {
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleAlertService.class);

    @Override
    public void sendAlert(String message, Exception ex) {
        Optional.ofNullable(message).ifPresent(m -> LOG.error(m));
        Optional.ofNullable(ex).ifPresent(e -> LOG.error(e.getMessage(), e));
    }
    
}
