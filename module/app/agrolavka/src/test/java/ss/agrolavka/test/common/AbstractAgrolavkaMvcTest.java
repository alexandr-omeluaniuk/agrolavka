package ss.agrolavka.test.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import ss.agrolavka.constants.CacheKey;
import ss.agrolavka.lucene.LuceneIndexer;
import ss.entity.agrolavka.Product;
import ss.entity.agrolavka.ProductsGroup;
import ss.martin.security.test.PlatformSecurityMvcTest;
import ss.martin.telegram.bot.api.TelegramBot;

import java.util.Optional;

@SpringBootTest(
        classes = AgrolavkaTestApplication.class,
        properties = {
                "mysklad.api.url=http://localhost:20233/mysklad"
        }
)
public abstract class AbstractAgrolavkaMvcTest extends PlatformSecurityMvcTest {
    
    @MockBean(name = "telegramBotOrders")
    protected TelegramBot telegramBotOrders;
    
    @MockBean(name = "telegramBotErrors")
    protected TelegramBot telegramBotErrors;

    @MockBean
    protected LuceneIndexer luceneIndexer;
    
    @Autowired
    private CacheManager cacheManager;
    
    @BeforeEach
    protected void before() {
        coreDao.massDelete(coreDao.getAll(Product.class));
        coreDao.massDelete(coreDao.getAll(ProductsGroup.class));
        Optional.ofNullable(cacheManager.getCache(CacheKey.PRODUCTS_GROUPS)).ifPresent(cache -> cache.clear());
    }
}
