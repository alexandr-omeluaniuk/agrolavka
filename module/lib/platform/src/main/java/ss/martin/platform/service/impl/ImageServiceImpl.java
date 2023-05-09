/*
 * The MIT License
 *
 * Copyright 2021 alex.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ss.entity.martin.EntityImage;
import ss.martin.platform.service.ImageService;
import ss.martin.security.configuration.external.PlatformConfiguration;

/**
 * Image service implementation.
 * @author alex
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ImageServiceImpl implements ImageService {
    /** Platform configuration. */
    @Autowired
    private PlatformConfiguration platformConfiguration;

    @Override
    public String saveImageToDisk(byte[] data) throws Exception {
        String randomName = generateRandomFilename();
        File file = new File(getRootFolder(), randomName);
        Files.write(Paths.get(file.toURI()), data, StandardOpenOption.CREATE_NEW);
        return randomName;
    }

    @Override
    public byte[] readImageFromDisk(EntityImage image) throws Exception {
        File file = new File(getRootFolder(), image.getFileNameOnDisk());
        return Files.readAllBytes(Paths.get(file.toURI()));
    }
    
    @Override
    public void deleteImageFromDisk(EntityImage image) throws Exception {
        File file = new File(getRootFolder(), image.getFileNameOnDisk());
        if (file.exists()) {
            file.delete();
        }
    }
    
    private File getRootFolder() {
        File folder = new File(platformConfiguration.getImagesStoragePath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
    
    private String generateRandomFilename() {
        return System.currentTimeMillis() + "." + UUID.randomUUID().toString();
    }
}
