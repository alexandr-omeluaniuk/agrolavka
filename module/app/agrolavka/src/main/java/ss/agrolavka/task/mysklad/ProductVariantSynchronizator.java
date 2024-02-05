package ss.agrolavka.task.mysklad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.ProductVariant;
import ss.martin.core.dao.CoreDao;

@Component
public class ProductVariantSynchronizator {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProductVariantSynchronizator.class);
    
    @Autowired
    private MySkladIntegrationService mySkladService;
    
    @Autowired
    private CoreDao coreDao;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void doImport() {
        final var variants = mySkladService.getProductVariants();
        coreDao.massDelete(coreDao.getAll(ProductVariant.class));
        coreDao.massCreate(variants);
        LOG.info("product variants imported [" + variants.size() + "]");
    }
}
