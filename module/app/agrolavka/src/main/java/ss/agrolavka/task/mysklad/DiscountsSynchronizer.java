package ss.agrolavka.task.mysklad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ss.agrolavka.dao.ProductDAO;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.Discount;
import ss.martin.core.dao.CoreDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DiscountsSynchronizer {

    private static final Logger LOG = LoggerFactory.getLogger(DiscountsSynchronizer.class);

    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;

    @Autowired
    private CoreDao coreDao;

    @Autowired
    private ProductDAO productDao;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, String> doImport() {
        final var existingDiscounts = coreDao.getAll(Discount.class).stream()
                .collect(Collectors.toMap(Discount::getExternalId, Function.identity()));
        LOG.info("existing discounts [" + existingDiscounts.size() + "]");
        final var mySkladDiscounts = mySkladIntegrationService.getDiscounts().stream()
                .collect(Collectors.toMap(Discount::getExternalId, Function.identity()));;
        LOG.info("MySklad discounts [" + mySkladDiscounts.size() + "]");
        final var discountsMap = new HashMap<String, String>();
        final var forUpdate = new ArrayList<Discount>();
        final var forCreate = new ArrayList<Discount>();
        for (final var discount : mySkladDiscounts.values()) {
            discount.getProducts().forEach(p -> {
                discountsMap.put(p.getExternalId(), discount.getExternalId());
            });
            if (existingDiscounts.containsKey(discount.getExternalId())) {
                final var existingDiscount = existingDiscounts.get(discount.getExternalId());
                existingDiscount.setDiscount(discount.getDiscount());
                existingDiscount.setName(discount.getName());
                forUpdate.add(existingDiscount);
            } else {
                discount.setProducts(null);
                forCreate.add(discount);
            }
        }
        LOG.info("discounts for update [" + forUpdate.size() + "]");
        coreDao.massUpdate(forUpdate);
        LOG.info("discounts for create [" + forCreate.size() + "]");
        coreDao.massCreate(forCreate);
        final var forDelete = existingDiscounts.values().stream()
                .filter(d -> !mySkladDiscounts.containsKey(d.getExternalId())).toList();
        LOG.info("discounts for delete [" + forDelete.size() + "]");
        if (!forDelete.isEmpty()) {
            productDao.resetDiscounts(forDelete);
        }
        coreDao.massDelete(forDelete);
        return discountsMap;
    }
}
