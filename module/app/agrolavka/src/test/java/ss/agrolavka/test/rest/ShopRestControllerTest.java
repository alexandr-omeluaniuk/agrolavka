package ss.agrolavka.test.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import ss.agrolavka.test.common.AgrolavkaDataFactory;
import static org.junit.jupiter.api.Assertions.*;
import ss.entity.agrolavka.Shop;
import ss.martin.core.model.EntitySearchResponse;

public class ShopRestControllerTest extends AbstractEntityWithImagesRestControllerTest<Shop> {
    
    @Override
    protected Class<Shop> entityClass() {
        return Shop.class;
    }

    @Override
    protected Shop generate() {
        return AgrolavkaDataFactory.generateShop();
    }

    @Override
    protected String endpoint() {
        return "/shop";
    }

    @Override
    protected Shop modifyEntityBeforeUpdate(Shop entity) {
        entity.setAddress("New address");
        entity.setDescription("New description");
        return entity;
    }

    @Override
    protected void verifyUpdatedEntity(Shop updatedEntity) {
        assertEquals("New address", updatedEntity.getAddress());
        assertEquals("New description", updatedEntity.getDescription());
    }

    @Override
    protected void entityAsserts(Shop shop) {
        assertNotNull(shop);
        assertTrue(shop.getId() > 0);
        assertNotNull(shop.getAddress());
        assertNotNull(shop.getDescription());
        assertNotNull(shop.getLatitude());
        assertNotNull(shop.getLongitude());
        assertNotNull(shop.getPhone());
        assertNotNull(shop.getTitle());
        assertNotNull(shop.getWorkingHours());
    }

    @Override
    protected TypeReference<EntitySearchResponse<Shop>> listType() {
        return new TypeReference<EntitySearchResponse<Shop>>() { };
    }
}
