package ss.agrolavka.test.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ss.agrolavka.constants.SiteUrls;
import ss.agrolavka.lucene.LuceneSearchResult;
import ss.agrolavka.service.SessionService;
import ss.agrolavka.test.common.AbstractAgrolavkaMvcTest;
import ss.agrolavka.test.common.AgrolavkaDataFactory;
import ss.agrolavka.wrapper.CartProduct;
import ss.agrolavka.wrapper.OrderDetailsWrapper;
import ss.agrolavka.wrapper.ProductsSearchResponse;
import ss.entity.agrolavka.Order;
import ss.entity.agrolavka.OrderPosition;
import ss.martin.security.test.DataFactory;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


public class AgrolavkaPublicRestControllerTest extends AbstractAgrolavkaMvcTest {

    @MockBean
    private SessionService sessionService;
    
    @Test
    public void testSearch() {
        DataFactory.silentAuthentication(coreDao);
        final var productGroup = coreDao.create(AgrolavkaDataFactory.generateProductGroup("New group"));
        coreDao.create(AgrolavkaDataFactory.generateProduct(productGroup, "Hummer", 100.0, 2.0));
        when(luceneIndexer.search(any())).thenReturn(new LuceneSearchResult(Collections.emptyList(), ""));

        final var response = callGet(
            SiteUrls.URL_PUBLIC + "/search?searchText=",
            ProductsSearchResponse.class,
            HttpStatus.OK
        );
        assertNotNull(response);
        assertEquals(0, response.count());
        assertEquals(0, response.data().size());

        final var response2 = callGet(
            SiteUrls.URL_PUBLIC + "/search?searchText=D", 
            ProductsSearchResponse.class, 
            HttpStatus.OK
        );
        assertNotNull(response2);
        assertEquals(0, response2.count());
        assertEquals(0, response2.data().size());

//        final var response3 = callGet(
//            SiteUrls.URL_PUBLIC + "/search?searchText=mm",
//            ProductsSearchResponse.class,
//            HttpStatus.OK
//        );
//        assertNotNull(response3);
//        assertEquals(1, response3.count());
//        assertEquals(1, response3.data().size());
    }

    @Test
    public void testAddToCart() {
        DataFactory.silentAuthentication(coreDao);
        final var productGroup = coreDao.create(AgrolavkaDataFactory.generateProductGroup("New group"));
        final var product = coreDao.create(
            AgrolavkaDataFactory.generateProduct(productGroup, "Hummer", 100.0, 2.0)
        );
        final var cartProduct = new CartProduct();
        cartProduct.setProductId(product.getId());
        cartProduct.setQuantity(20d);
        final var cart = new Order();
        cart.setPositions(new ArrayList<>());
        when(sessionService.getCurrentOrder(any())).thenReturn(cart);

        final var order = callPut(
            SiteUrls.URL_PUBLIC + "/cart",
            cartProduct,
            Order.class,
            HttpStatus.OK
        );
        assertNotNull(order);
        assertEquals(1, order.getPositions().size());
    }

    @Test
    public void testCreateOrder() {
        DataFactory.silentAuthentication(coreDao);
        final var productGroup = coreDao.create(AgrolavkaDataFactory.generateProductGroup("New group"));
        final var product = coreDao.create(
            AgrolavkaDataFactory.generateProduct(productGroup, "Hummer", 100.0, 2.0)
        );
        final var payload = new OrderDetailsWrapper();
        payload.setPhone("+375-29-785-27-22");
        final var cart = new Order();
        cart.setPositions(new ArrayList<>());
        final var position = new OrderPosition();
        position.setProductId(product.getId());
        position.setPrice(product.getPrice());
        position.setQuantity(20);
        position.setOrder(cart);
        cart.getPositions().add(position);
        when(sessionService.getCurrentOrder(any())).thenReturn(cart);

        final var order = callPost(
            SiteUrls.URL_PUBLIC + "/order",
            payload,
            Order.class,
            HttpStatus.OK
        );
        assertNotNull(order);
        assertEquals(1, order.getPositions().size());
        assertNotNull(order.getId());
    }
}
