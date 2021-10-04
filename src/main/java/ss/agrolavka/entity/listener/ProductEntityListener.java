/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.entity.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ss.agrolavka.constants.SiteConstants;
import ss.agrolavka.service.MySkladIntegrationService;
import ss.entity.agrolavka.Product;
import ss.entity.martin.EntityImage;
import ss.martin.platform.dao.CoreDAO;
import ss.martin.platform.service.ImageService;
import ss.martin.platform.util.PlatformEntityListener;

/**
 * Product entity listener.
 * @author alex
 */
@Component
@Qualifier("ProductEntityListener")
class ProductEntityListener implements PlatformEntityListener<Product> {
    /** MySklad integration service. */
    @Autowired
    private MySkladIntegrationService mySkladIntegrationService;
    /** Core DAO. */
    @Autowired
    private CoreDAO coreDAO;
    /** Image service. */
    @Autowired
    private ImageService imageService;

    @Override
    public Class entity() {
        return Product.class;
    }

    @Override
    public void prePersist(Product entity) throws Exception {
        Product mySkladEntity = mySkladIntegrationService.createProduct(entity);
        for (EntityImage image : entity.getImages()) {
            byte[] thumb = imageService.convertToThumbnail(image.getData(), SiteConstants.IMAGE_THUMB_SIZE);
            image.setData(thumb);
        }
        entity.setExternalId(mySkladEntity.getExternalId());
        mySkladIntegrationService.attachImagesToProduct(entity);
    }
    
    @Override
    public void preUpdate(Product entity) throws Exception {
        mySkladIntegrationService.updateProduct(entity);
        Product entityFromDB = coreDAO.findById(entity.getId(), Product.class);
        Map<Long, EntityImage> map = entityFromDB.getImages().stream()
                .collect(Collectors.toMap(EntityImage::getId, Function.identity()));
        List<EntityImage> actualImages = new ArrayList();
        for (EntityImage image : entity.getImages()) {
            if (image.getData() != null) {
                byte[] thumb = imageService.convertToThumbnail(image.getData(), SiteConstants.IMAGE_THUMB_SIZE);
                image.setData(thumb);
                actualImages.add(image);
            } else if (image.getId() != null && map.containsKey(image.getId())) {
                actualImages.add(map.get(image.getId()));
            }
        }
        entityFromDB.setImages(actualImages);
        entity.setImages(actualImages);
        coreDAO.update(entityFromDB);
        mySkladIntegrationService.removeProductImages(entity);
        mySkladIntegrationService.attachImagesToProduct(entity);
    }
    
    @Override
    public void preDelete(Set<Long> ids) throws Exception {
        for (Long id : ids) {
            Product product = coreDAO.findById(id, Product.class);
            mySkladIntegrationService.deleteProduct(product);
        }
    }
    
}
