package ss.agrolavka.task.mysklad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.Agent;
import ss.martin.core.dao.CoreDao;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AgentsSynchronizer {

    private static final Logger LOG = LoggerFactory.getLogger(AgentsSynchronizer.class);

    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;

    @Autowired
    private CoreDao coreDao;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void doImport() {
        final var existingAgents = coreDao.getAll(Agent.class).stream()
                .collect(Collectors.toMap(Agent::getExternalId, Function.identity()));
        LOG.info("existing agents [" + existingAgents.size() + "]");
        final var mySkladAgents = new ArrayList<Agent>();
        int offset = 0;
        while (offset < 10000) {
            LOG.info("agents portion: " + offset + " - " + (offset + 1000));
            List<Agent> chunk = mySkladIntegrationService.getAgents(offset, 1000);
            if (chunk.isEmpty()) {
                break;
            }
            mySkladAgents.addAll(chunk);
            offset += 1000;
        }
        final var agents = mySkladAgents.stream()
                .collect(Collectors.toMap(Agent::getExternalId, Function.identity()));;
        LOG.info("MySklad agents [" + agents.size() + "]");
        final var forUpdate = new ArrayList<Agent>();
        final var forCreate = new ArrayList<Agent>();
        for (final var agent : agents.values()) {
            if (existingAgents.containsKey(agent.getExternalId())) {
                final var existingAgent = existingAgents.get(agent.getExternalId());
                existingAgent.setPhone(agent.getPhone());
                existingAgent.setName(agent.getName());
                forUpdate.add(existingAgent);
            } else {
                forCreate.add(agent);
            }
        }
        LOG.info("agents for update [" + forUpdate.size() + "]");
        coreDao.massUpdate(forUpdate);
        LOG.info("agents for create [" + forCreate.size() + "]");
        coreDao.massCreate(forCreate);
        final var forDelete = existingAgents.values().stream()
                .filter(d -> !agents.containsKey(d.getExternalId())).toList();
        LOG.info("agents for delete [" + forDelete.size() + "]");
        coreDao.massDelete(forDelete);
    }
}
