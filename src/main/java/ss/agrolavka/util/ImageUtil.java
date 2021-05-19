/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import net.coobird.thumbnailator.Thumbnails;
import ss.entity.martin.EntityImage;

/**
 * Images utility class.
 * @author alex
 */
public class ImageUtil {
    /** Thumbnail min size. */
    private static final int THUMBNAIL_MIN_SIZE = 306;
    /**
     * Convert entity image to thumbnail.
     * @param images entity images.
     * @throws Exception error.
     */
    public void toThumbnail(List<EntityImage> images) throws Exception {
        if (images != null) {
            for (EntityImage image : images) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Thumbnails.Builder is = Thumbnails.of(new ByteArrayInputStream(image.getData()));
                BufferedImage bufImage = is.scale(1d).asBufferedImage();
                int height = bufImage.getHeight();
                int width = bufImage.getWidth();
                is = Thumbnails.of(new ByteArrayInputStream(image.getData()));
                if (height < width) {
                    is.height(THUMBNAIL_MIN_SIZE).toOutputStream(baos);
                } else {
                    is.width(THUMBNAIL_MIN_SIZE).toOutputStream(baos);
                }
                image.setData(baos.toByteArray());
                baos.close();
            }
        }
    }
}
