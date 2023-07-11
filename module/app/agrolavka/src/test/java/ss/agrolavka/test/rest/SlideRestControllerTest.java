package ss.agrolavka.test.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import ss.entity.agrolavka.Slide;
import ss.martin.core.model.EntitySearchResponse;
import static org.junit.jupiter.api.Assertions.*;

public class SlideRestControllerTest extends AbstractEntityWithImagesRestControllerTest<Slide> {

    @Override
    protected Class<Slide> entityClass() {
        return Slide.class;
    }

    @Override
    protected TypeReference<EntitySearchResponse<Slide>> listType() {
        return new TypeReference<EntitySearchResponse<Slide>>() { };
    }

    @Override
    protected Slide generate() {
        final var slide = new Slide();
        slide.setName("My slide");
        slide.setButtonLink("http://host.test/somelink");
        slide.setButtonText("My button");
        slide.setTitle("The best title");
        slide.setSubtitle("The best subtitle");
        return slide;
    }

    @Override
    protected String endpoint() {
        return "/slide";
    }

    @Override
    protected Slide modifyEntityBeforeUpdate(Slide entity) {
        entity.setTitle("Wow");
        entity.setButtonText("New button text");
        return entity;
    }

    @Override
    protected void verifyUpdatedEntity(Slide updatedEntity) {
        assertEquals("Wow", updatedEntity.getTitle());
        assertEquals("New button text", updatedEntity.getButtonText());
    }

    @Override
    protected void entityAsserts(Slide entity) {
        assertNotNull(entity.getButtonLink());
        assertNotNull(entity.getButtonText());
        assertNotNull(entity.getName());
        assertNotNull(entity.getSubtitle());
        assertNotNull(entity.getTitle());
    }
    
}
