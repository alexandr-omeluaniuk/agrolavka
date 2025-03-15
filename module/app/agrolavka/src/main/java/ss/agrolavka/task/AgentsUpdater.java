package ss.agrolavka.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ss.agrolavka.service.MySkladIntegrationService;

@Component
public class AgentsUpdater {

    @Autowired
    private MySkladIntegrationService mySklad;

    @Scheduled(cron = "0 30 * * * *")
    public void importAgents() {

    }
}
