package ss.martin.core.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ss.entity.martin.Subscription;
import ss.entity.martin.Subscription_;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.test.AbstractComponentTest;

public class CoreDAOTest extends AbstractComponentTest {
    
    @Autowired
    private CoreDao coreDAO;
    
    @BeforeEach
    public void beforeEach() {
        coreDAO.massDelete(coreDAO.getAll(Subscription.class));
    }
    
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
    public void testFindByIds() {
        final var entity = coreDAO.create(generateEntity());
        assertNotNull(entity);
        assertNotNull(entity.getId());
        
        final var list = coreDAO.findByIds(new HashSet<>(Arrays.asList(entity.getId())), Subscription.class);
        
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertNotNull(list.get(0).getId());
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
    public void testSearchEntities_Paging() {
        final var list = Arrays.asList(generateEntity(), generateEntity(), generateEntity());
        coreDAO.massCreate(list);
        final var pageSize = 2;
        
        final var response = coreDAO.searchEntities(new EntitySearchRequest(1, pageSize), Subscription.class);
        
        assertEquals(list.size(), response.total());
        assertEquals(pageSize, response.data().size());
    }
    
    @Test
    public void testSearchEntities_PagingIgnoreCount() {
        final var list = Arrays.asList(generateEntity(), generateEntity(), generateEntity());
        coreDAO.massCreate(list);
        final var pageSize = 2;
        
        final var response = coreDAO.searchEntities(new EntitySearchRequest(1, pageSize, null, null, true, false), Subscription.class);
        
        assertEquals(0, response.total());
        assertEquals(pageSize, response.data().size());
    }
    
    @Test
    public void testSearchEntities_ShowDeactivated() {
        final var list = Arrays.asList(generateEntity(), generateEntity(), generateEntity());
        coreDAO.massCreate(list);
        final var ids = list.stream().map(Subscription::getId).collect(Collectors.toSet());
        coreDAO.deactivateEntities(ids, Subscription.class);
        final var pageSize = 2;
        
        final var responseActive = coreDAO.searchEntities(new EntitySearchRequest(1, pageSize, null, null, false, false), Subscription.class);
        
        assertEquals(0, responseActive.total());
        assertEquals(0, responseActive.data().size());
        
        final var responseDeactivated = coreDAO.searchEntities(new EntitySearchRequest(1, pageSize, null, null, false, true), Subscription.class);
        
        assertEquals(list.size(), responseDeactivated.total());
        assertEquals(pageSize, responseDeactivated.data().size());
    }
    
    @Test
    public void testSearchEntities_SortDesc() {
        final var list = Arrays.asList(generateEntity("T1"), generateEntity("T2"), generateEntity("T3"));
        coreDAO.massCreate(list);
        
        final var response = coreDAO.searchEntities(new EntitySearchRequest(1, 100, null, Subscription_.ORGANIZATION_NAME), Subscription.class);
        
        assertEquals(list.size(), response.total());
        assertEquals(list.size(), response.data().size());
        assertEquals("T3", response.data().get(0).getOrganizationName());
        assertEquals("T2", response.data().get(1).getOrganizationName());
        assertEquals("T1", response.data().get(2).getOrganizationName());
    }
    
    @Test
    public void testSearchEntities_SortAsc() {
        final var list = Arrays.asList(generateEntity("T1"), generateEntity("T2"), generateEntity("T3"));
        coreDAO.massCreate(list);
        
        final var response = coreDAO.searchEntities(new EntitySearchRequest(1, 100, "asc", Subscription_.ORGANIZATION_NAME), Subscription.class);
        
        assertEquals(list.size(), response.total());
        assertEquals(list.size(), response.data().size());
        assertEquals("T1", response.data().get(0).getOrganizationName());
        assertEquals("T2", response.data().get(1).getOrganizationName());
        assertEquals("T3", response.data().get(2).getOrganizationName());
    }
    
    @Test
    public void testDeactivateAndActivateEntities() {
        final var list = Arrays.asList(generateEntity(), generateEntity(), generateEntity());
        coreDAO.massCreate(list);
        final var ids = list.stream().map(Subscription::getId).collect(Collectors.toSet());
        
        coreDAO.deactivateEntities(ids, Subscription.class);
        
        final var allDeactivated = coreDAO.getAll(Subscription.class);
        assertEquals(list.size(), allDeactivated.size());
        allDeactivated.forEach(entity -> assertFalse(entity.isActive()));
        
        coreDAO.activateEntities(ids, Subscription.class);
        final var allActivated = coreDAO.getAll(Subscription.class);
        assertEquals(list.size(), allActivated.size());
        allActivated.forEach(entity -> assertTrue(entity.isActive()));
    }
    
    @Test
    public void testDeactivateAndActivateEntities_EmptyList() {
        assertDoesNotThrow(() -> coreDAO.deactivateEntities(Collections.emptySet(), Subscription.class));
        assertDoesNotThrow(() -> coreDAO.activateEntities(Collections.emptySet(), Subscription.class));
    }
    
    @Test
    public void testCount() {
        final var list = Arrays.asList(generateEntity(), generateEntity(), generateEntity());
        coreDAO.massCreate(list);
        
        final var count = coreDAO.count(Subscription.class);
        
        assertEquals(list.size(), count);
    }
    
    private Subscription generateEntity(final String orgName) {
        final var subscription = new Subscription();
        subscription.setActive(true);
        subscription.setExpirationDate(new Date());
        subscription.setOrganizationName(orgName);
        subscription.setStarted(new Date());
        subscription.setSubscriptionAdminEmail("test@test.email");
        return subscription;
    }
    
    private Subscription generateEntity() {
        return generateEntity("Test org.");
    }
}
