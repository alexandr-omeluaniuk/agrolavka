package ss.agrolavka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ss.agrolavka.dao.ProductAttributeLinkDao;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductAttribute;
import ss.entity.agrolavka.ProductAttributeItem;
import ss.entity.agrolavka.ProductAttributeLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductAttributesService {

    @Autowired
    private ProductAttributeLinkDao productAttributeLinkDao;

    public List<Product> setAttributeLinks(List<Product> products) {
        products.forEach(p -> p.setAttributeLinks(new ArrayList<>()));
        final var productsMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        productAttributeLinkDao.getAllLinks().forEach(link -> {
            if (productsMap.containsKey(link.getProductId())) {
                final var product = productsMap.get(link.getProductId());
                product.getAttributeLinks().add(link);
            }
        });
        return products;
    }

    public Set<Long> getProductIds(ProductAttributeItem item) {
        return productAttributeLinkDao.getAllLinks().stream()
            .filter(link -> link.getAttributeItem().equals(item))
            .map(ProductAttributeLink::getProductId)
            .collect(Collectors.toSet());
    }

    public ProductAttributeItem findByUrl(String attributeUrl, String itemUrl) {
        final var result = productAttributeLinkDao.getAllLinks().stream().filter(link -> {
            final var isAttributeMatch = attributeUrl.equals(link.getAttributeItem().getProductAttribute().getUrl());
            final var isItemMatch = itemUrl.equals(link.getAttributeItem().getUrl());
            return isAttributeMatch && isItemMatch;
        }).findFirst();
        return result.map(ProductAttributeLink::getAttributeItem).orElse(null);
    }

    public List<ProductAttribute> getAttributeGroups() {
        return productAttributeLinkDao.getAllLinks()
            .stream().map(link -> link.getAttributeItem().getProductAttribute()).distinct()
            .sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toCollection(ArrayList::new));
    }
}
