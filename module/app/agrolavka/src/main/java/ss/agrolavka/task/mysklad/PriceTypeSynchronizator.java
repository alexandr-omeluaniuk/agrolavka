package ss.agrolavka.task.mysklad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.ExternalEntityDAO;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.PriceType;
import ss.martin.core.dao.CoreDao;

@Component
public class PriceTypeSynchronizator {
    
    private static final Logger LOG = LoggerFactory.getLogger(PriceTypeSynchronizator.class);
    
    @Autowired
    private MySkladIntegrationService mySkladService;
    
    @Autowired
    private CoreDao coreDao;
    
    @Autowired
    private ExternalEntityDAO externalEntityDAO;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void doImport() {
        final var priceTypes = mySkladService.getPriceTypes();
        final var priceTypeMap = priceTypes.stream().collect(
            Collectors.toMap(PriceType::getExternalId, Function.identity())
        );
        final var actualPriceTypesIDs = priceTypeMap.keySet();
        final var deleteNotIn = new HashSet<>(priceTypeMap.keySet());
        final var existPriceTypesIDs = new HashSet<>();
        final var existingPriceTypes = externalEntityDAO.getExternalEntitiesByIds(
            priceTypeMap.keySet(), 
            PriceType.class
        );
        for (final var existingPriceType : existingPriceTypes) {
            final var freshPriceType = priceTypeMap.get(existingPriceType.getExternalId());
            existingPriceType.setName(freshPriceType.getName());
            existPriceTypesIDs.add(existingPriceType.getExternalId());
        }
        
        actualPriceTypesIDs.removeAll(existPriceTypesIDs);
        final var newPriceTypes = new ArrayList<PriceType>();
        for (final var newExternalId : actualPriceTypesIDs) {
            final var newPriceType = priceTypeMap.get(newExternalId);
            LOG.info("create new price type: " + newPriceType);
            newPriceTypes.add(newPriceType);
        }
        LOG.info("existing price types [" + existingPriceTypes.size() + "]");
        LOG.info("new price types [" + newPriceTypes.size() + "]");
        LOG.info("delete price types [" + deleteNotIn + "]");
        coreDao.massUpdate(existingPriceTypes);
        coreDao.massCreate(newPriceTypes);
        externalEntityDAO.removeExternalEntitiesNotInIDs(deleteNotIn, PriceType.class);
    }
}
