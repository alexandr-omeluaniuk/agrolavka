/*
 * Copyright (C) 2021 alex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ss.agrolavka.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ss.agrolavka.service.BackupService;
import ss.martin.images.storage.configuration.external.StorageConfiguration;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Backup service implementation.
 * @author alex
 */
@Service
class BackupServiceImpl implements BackupService {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(BackupServiceImpl.class);
    /** Backup directory. */
    private static final File BACKUP_DIR = new File("/tmp/agrolavka");
    /** DB username. */
    @Value("${spring.datasource.username}")
    private String dbUser;
    /** DB password. */
    @Value("${spring.datasource.password}")
    private String dbPassword;
    /** Platform configuration. */
    @Autowired
    private StorageConfiguration storageConfiguration;

    @Value("${gdrive.pups}")
    private String gdrivePups;
    @Value("${gdrive.store}")
    private String gdriveStore;
    @Value("${gdrive.aname}")
    private String gdriveAname;
//    /** Email service. */
//    @Autowired
//    private EmailService emailService;
    @Override
    public File doBackup() throws Exception {
        if (!BACKUP_DIR.exists()) {
            BACKUP_DIR.mkdirs();
        }
        File dumpFile = mysqlDump();
        LOG.info("SQL Dump file path: " + dumpFile.getAbsolutePath());
        File imagesZip = imagesBackup();
        LOG.info("Images zip file path: " + imagesZip.getAbsolutePath());
        File backupFile = backupArchive();
        LOG.info("Backup archive file path: " + backupFile.getAbsolutePath());
//        // attach to email
//        EmailRequest email = new EmailRequest();
//        email.setSubject("Agrolavka backup");
//        email.setMessage("Data backup: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()));
//        email.setRecipients(new EmailRequest.EmailContact[] {
//            new EmailRequest.EmailContact("Alex", "starshistrelok@gmail.com")
//        });
//        email.setAttachments(new EmailRequest.EmailAttachment[]{
//            new EmailRequest.EmailAttachment("agrolavka_backup.zip", "application/zip", backupFile)
//        });
//        email.setSender(
//            new EmailRequest.EmailContact(platformConfig.getSystemEmailContactName(),
//                platformConfig.getSystemEmailContactEmail()));
//        emailService.sendEmail(email);
//        LOG.info("email sent...");
        return backupFile;
    }

    @PostConstruct
    private void init() throws Exception {
        createBackup();
    }

    @Override
    public void createBackup() throws Exception {
        final var drive = getInstance();
    }

    private File backupArchive() throws Exception {
        File backupFile = new File("/tmp", "agrolavka_backup.zip");
        try (FileOutputStream fos = new FileOutputStream(backupFile); ZipOutputStream zos = new ZipOutputStream(fos)) {
            addDirToZipArchive(zos, BACKUP_DIR, null);
            zos.flush();
            fos.flush();
        }
        return backupFile;
    }
    
    private File imagesBackup() throws Exception {
        File archive = new File(BACKUP_DIR, "agrolavka_images.zip");
        try (FileOutputStream fos = new FileOutputStream(archive); ZipOutputStream zos = new ZipOutputStream(fos)) {
            addDirToZipArchive(zos, new File(storageConfiguration.path()), null);
            zos.flush();
            fos.flush();
        }
        return archive;
    }
    
    private File mysqlDump() throws Exception {
        File dumpFile = new File(BACKUP_DIR, "agrolavka.sql");
        String command = "mysqldump -u" + dbUser + " -p" + dbPassword + " agrolavka -r " + dumpFile.getAbsolutePath();
        LOG.info("Command: " + command);
        Process process = Runtime.getRuntime().exec(command);
        int code = process.waitFor();
        LOG.info("mysqldump code: " + code);
        if (code != 0) {
            byte[] errData = process.getErrorStream().readAllBytes();
            LOG.info("error: " + new String(errData, "UTF-8"));
        }
        return dumpFile;
    }
    
    public static void addDirToZipArchive(ZipOutputStream zos, File fileToZip, String parrentDirectoryName)
            throws Exception {
        if (fileToZip == null || !fileToZip.exists()) {
            return;
        }
        String zipEntryName = fileToZip.getName();
        if (parrentDirectoryName != null && !parrentDirectoryName.isEmpty()) {
            zipEntryName = parrentDirectoryName + "/" + fileToZip.getName();
        }
        if (fileToZip.isDirectory()) {
            LOG.trace("+" + zipEntryName);
            for (File file : fileToZip.listFiles()) {
                addDirToZipArchive(zos, file, zipEntryName);
            }
        } else {
            LOG.trace("   " + zipEntryName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(fileToZip);
            zos.putNextEntry(new ZipEntry(zipEntryName));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
        }
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
        throws IOException {
        final var jsonFactory = GsonFactory.getDefaultInstance();
        InputStream in = new FileInputStream(gdrivePups);
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, jsonFactory, clientSecrets, Collections.singletonList(DriveScopes.DRIVE_FILE))
            .setDataStoreFactory(new FileDataStoreFactory(new File(gdriveStore)))
            .setAccessType("offline")
            .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        receiver.stop();
        return credential;
    }

    private Drive getInstance() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, GsonFactory.getDefaultInstance(), getCredentials(HTTP_TRANSPORT))
            .setApplicationName(gdriveAname)
            .build();
        return service;
    }
}
