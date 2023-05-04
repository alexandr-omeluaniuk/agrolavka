package ss.martin.core.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ss.entity.martin.Subscription;
import ss.martin.core.test.TestMain;

@SpringBootTest(classes = TestMain.class)
public class CoreDAOTest {
    
    @Autowired
    private CoreDao coreDAO;
    
    @Test
    public void testCreate() {
        final var entity = coreDAO.create(generateEntity());
        
        assertNotNull(entity);
        assertNotNull(entity.getId());
    }
    
    @Test
    public void testUpdate() {
        final var entity = coreDAO.create(generateEntity());
        assertNotNull(entity);
        assertNotNull(entity.getId());
        final var orgName = "Bloom";
        entity.setOrganizationName(orgName);
        
        coreDAO.update(entity);
        
        final var updatedEntity = coreDAO.findById(entity.getId(), Subscription.class);
        assertEquals(orgName, updatedEntity.getOrganizationName());
    }
    
    @Test
    public void testFindById() {
        final var entity = coreDAO.create(generateEntity());
        assertNotNull(entity);
        assertNotNull(entity.getId());
        
        final var retrievedEntity = coreDAO.findById(entity.getId(), Subscription.class);
        
        assertNotNull(retrievedEntity);
        assertNotNull(retrievedEntity.getId());
    }
    
    @Test
    public void testDelete() {
        final var entity = coreDAO.create(generateEntity());
        assertNotNull(entity);
        assertNotNull(entity.getId());
        
        coreDAO.delete(entity.getId(), Subscription.class);
        
        final var retrievedEntity = coreDAO.findById(entity.getId(), Subscription.class);
        assertNull(retrievedEntity);
    }
    
    @Test
    public void testMassDelete() {
        final var list = Arrays.asList(coreDAO.create(generateEntity()), coreDAO.create(generateEntity()), coreDAO.create(generateEntity()));
        
        coreDAO.massDelete(list);
        
        list.forEach(entity -> assertNull(coreDAO.findById(entity.getId(), Subscription.class)));
    }
    
    @Test
    public void testMassDelete_Empty() {
        assertDoesNotThrow(() -> coreDAO.massDelete(Collections.emptyList()));
    }
    
    @Test
    public void testMassDelete_EmptyAlt() {
        assertDoesNotThrow(() -> coreDAO.massDelete(Collections.emptySet(), Subscription.class));
    }
    
    @Test
    public void testMassCreate() {
        final var list = Arrays.asList(generateEntity(), generateEntity(), generateEntity());
        
        coreDAO.massCreate(list);
        
        list.forEach(entity -> assertNotNull(entity.getId()));
    }
    
    @Test
    public void testMassUpdate() {
        final var list = Arrays.asList(generateEntity(), generateEntity(), generateEntity());
        coreDAO.massCreate(list);
        final var orgName = "Luna";
        list.forEach(entity -> entity.setOrganizationName(orgName));
        
        coreDAO.massUpdate(list);
        
        list.forEach(entity -> assertEquals(orgName, coreDAO.findById(entity.getId(), Subscription.class).getOrganizationName()));
    }
    
    @Test
    public void testSearchEntities() {
        final var list = Arrays.asList(generateEntity(), generateEntity(), generateEntity());
        coreDAO.massCreate(list);
    }
    
    private Subscription generateEntity() {
        final var subscription = new Subscription();
        subscription.setActive(true);
        subscription.setExpirationDate(new Date());
        subscription.setOrganizationName("Test org.");
        subscription.setStarted(new Date());
        subscription.setSubscriptionAdminEmail("test@test.email");
        return subscription;
    }
}
