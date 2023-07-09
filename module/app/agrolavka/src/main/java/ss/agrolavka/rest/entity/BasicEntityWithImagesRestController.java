package ss.agrolavka.rest.entity;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ss.entity.agrolavka.EntityWithImages;
import ss.entity.images.storage.EntityImage;
import ss.entity.martin.DataModel;

/**
 * Basic REST controller for entity with images.
 * @author alex
 * @param <T> entity type.
 */
public abstract class BasicEntityWithImagesRestController<T extends DataModel & EntityWithImages> 
    extends BasicEntityRestController<T> {
    
    @PostMapping
    public T create(
        @RequestPart(value = "image", required = false) final MultipartFile[] files, 
        @RequestPart(value = "entity", required = true) final T entity
    ) {
        setImages(entity, files);
        final var newEntity = coreDAO.create(entity);
        return newEntity;
    }
    
    @PutMapping
    public T update(
        @RequestPart(value = "image", required = false) final MultipartFile[] files, 
        @RequestPart(value = "entity", required = true) final T entity
    ) {
        setImages(entity, files);
        final var updatedEntity = coreDAO.update(entity);
        return updatedEntity;
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        coreDAO.delete(id, entityClass());
    }
    
    private void setImages(final EntityWithImages entity, final MultipartFile[] files) {
        entity.setImages(Optional.ofNullable(entity.getImages()).orElse(new ArrayList<>()));
        Optional.ofNullable(files).ifPresent(filesArray ->
            entity.getImages().addAll(Stream.of(filesArray).map(file ->
                    new EntityImage(file)
                ).collect(Collectors.toList())
            )
        );
    }
}
