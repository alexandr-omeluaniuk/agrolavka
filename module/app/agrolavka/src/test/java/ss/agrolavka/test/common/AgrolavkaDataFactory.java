package ss.agrolavka.test.common;

import java.util.UUID;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.Product;

public class AgrolavkaDataFactory {
    
    public static Product generateProduct(final String name, final Double price, final Double quantity) {
        final var product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setUrl(UrlProducer.buildProductUrl(product));
        product.setBuyPrice(price);
        product.setExternalId(UUID.randomUUID().toString());
        return product;
    }
}
