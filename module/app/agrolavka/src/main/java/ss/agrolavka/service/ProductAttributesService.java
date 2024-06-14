package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.agrolavka.dao.ProductAttributeLinkDao;
import ss.entity.agrolavka.Product;
import ss.martin.core.dao.CoreDao;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductAttributesService {

    @Autowired
    private ProductAttributeLinkDao productAttributeLinkDao;

    @Autowired
    private CoreDao coreDao;

    public void setAttributeLinks(List<Product> products) {
        final var productsMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        productAttributeLinkDao.getAllLinks().forEach(link -> {
            if (productsMap.containsKey(link.getProductId())) {
                final var product = productsMap.get(link.getProductId());
                if (product.getAttributeLinks() == null) {
                    product.setAttributeLinks(new ArrayList<>());
                }
                product.getAttributeLinks().add(link);
            }
        });
    }
}
