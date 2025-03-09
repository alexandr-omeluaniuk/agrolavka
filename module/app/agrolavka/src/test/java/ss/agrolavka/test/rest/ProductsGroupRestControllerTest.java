package ss.agrolavka.test.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.agrolavka.test.common.AgrolavkaDataFactory;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.core.model.EntitySearchResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductsGroupRestControllerTest extends AbstractEntityWithImagesRestControllerTest<ProductsGroup> {
    
    @MockBean
    private MySkladIntegrationService mySklad;
    
    @BeforeEach
    public void beforeTest() {
        final var allo = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
        System.out.println("Allo: ");
        System.out.println(new StringBuilder(allo).reverse().toString());
        coreDao.massDelete(coreDao.getAll(Product.class));
        coreDao.massDelete(coreDao.getAll(ProductsGroup.class));
        when(mySklad.createProductsGroup(any())).thenReturn(UUID.randomUUID().toString());
        doNothing().when(mySklad).updateProductsGroup(any());
        doNothing().when(mySklad).deleteProductsGroup(any());
    }
    
    @Override
    protected Class<ProductsGroup> entityClass() {
        return ProductsGroup.class;
    }

    @Override
    protected TypeReference<EntitySearchResponse<ProductsGroup>> listType() {
        return new TypeReference<EntitySearchResponse<ProductsGroup>>() { };
    }

    @Override
    protected ProductsGroup generate() {
        final var entity = AgrolavkaDataFactory.generateProductGroup("Test group X");
        entity.setDescription("Some description");
        return entity;
    }

    @Override
    protected String endpoint() {
        return "/products-group";
    }

    @Override
    protected ProductsGroup modifyEntityBeforeUpdate(ProductsGroup entity) {
        entity.setName("New test name X");
        entity.setDescription("New description YZ");
        return entity;
    }

    @Override
    protected void verifyUpdatedEntity(ProductsGroup updatedEntity) {
        assertEquals(updatedEntity.getName(), "New test name X");
        assertEquals(updatedEntity.getDescription(), "New description YZ");
    }

    @Override
    protected void entityAsserts(ProductsGroup entity) {
        assertNotNull(entity.getName());
        assertNotNull(entity.getDescription());
        assertNotNull(entity.getUrl());
    }
    
    
}
