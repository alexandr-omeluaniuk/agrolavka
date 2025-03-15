package ss.agrolavka.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ss.agrolavka.AgrolavkaConfiguration;
import ss.agrolavka.task.mysklad.AgentsSynchronizer;
import ss.martin.security.service.SecurityService;

@Component
public class AgentsUpdater {

    /** Agrolavka configuration. */
    @Autowired
    private AgrolavkaConfiguration configuration;

    /** Security service. */
    @Autowired
    private SecurityService securityService;

    @Autowired
    private AgentsSynchronizer synchronizer;

    @Scheduled(cron = "0 1 * * * *")
    public void importAgents() {
        securityService.backgroundAuthentication(
                configuration.backgroundUserUsername(),
                configuration.backgroundUserPassword()
        );
        synchronizer.doImport();
    }
}
