package ss.agrolavka.task.mysklad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductVariant;
import ss.martin.core.dao.CoreDao;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductVariantSynchronizator {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProductVariantSynchronizator.class);
    
    @Autowired
    private MySkladIntegrationService mySkladService;
    
    @Autowired
    private CoreDao coreDao;

    @Autowired
    private ProductDAO productDAO;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void doImport() {
        final var variants = mySkladService.getProductVariants();
        final var productExternalIds = variants.stream().map(ProductVariant::getParentId)
            .collect(Collectors.toList());
        final var productsMap = productDAO.getByExternalIds(productExternalIds).stream()
            .collect(Collectors.toMap(Product::getExternalId, Function.identity()));
        variants.forEach(v -> {
            if (productsMap.containsKey(v.getParentId())) {
                final var product = productsMap.get(v.getParentId());
                v.setName(ProductVariant.createProductWithModificationsName(product.getName()));
            }
        });
        coreDao.massDelete(coreDao.getAll(ProductVariant.class));
        coreDao.massCreate(variants);
        LOG.info("product variants imported [" + variants.size() + "]");
    }
}
