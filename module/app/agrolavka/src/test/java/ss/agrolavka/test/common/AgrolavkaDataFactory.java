package ss.agrolavka.test.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import ss.agrolavka.constants.OrderStatus;
import ss.agrolavka.util.UrlProducer;
import ss.entity.agrolavka.Address;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.entity.agrolavka.Shop;

public class AgrolavkaDataFactory {
    
    public static ProductsGroup generateProductGroup(final String name) {
        final var group = new ProductsGroup();
        group.setName(name);
        group.setExternalId(UUID.randomUUID().toString());
        group.setUrl(UrlProducer.transliterate(name));
        return group;
    }
    
    public static Product generateProduct(
        final ProductsGroup group, final String name, final Double price, final Double quantity
    ) {
        final var product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setUrl(UrlProducer.transliterate(name));
        product.setBuyPrice(price);
        product.setExternalId(UUID.randomUUID().toString());
        product.setGroup(group);
        return product;
    }
    
    public static Order generateOrder() {
        final var order = new Order();
        order.setPositions(new ArrayList<>());
        order.setPhone("29 796-04-03");
        order.setOneClick(null);
        order.setStatus(OrderStatus.WAITING_FOR_APPROVAL);
        order.setCreated(new Date());
        return order;
    }
    
    public static Address generateAddress() {
        final var address = new Address();
        address.setFirstname("Петр");
        address.setLastname("Иванов");
        address.setMiddlename("Васильевич");
        address.setCity("Борисов");
        address.setDistrict("Борисовский райен");
        address.setFlat("20");
        address.setPostcode("224000");
        address.setRegion("Минская область");
        address.setHouse("33");
        address.setStreet("Вишневая");
        return address;
    }
    
    public static OrderPosition generateOrderPosition(final Product product) {
        final var position = new OrderPosition();
        position.setProduct(product);
        position.setProductId(product.getId());
        position.setPrice(product.getPrice());
        position.setQuantity(1);
        return position;
    }
    
    public static Shop generateShop() {
        final var shop = new Shop();
        shop.setAddress("Brest, Rabinovaya st. 31");
        shop.setDescription("The best shop in the world");
        shop.setLatitude(52.0);
        shop.setLongitude(28.0);
        shop.setWorkingHours("Some working hours");
        shop.setTitle("Main shop");
        shop.setPhone("+375 666 66 66");
        return shop;
    }
}
