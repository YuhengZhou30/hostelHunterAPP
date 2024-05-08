package com.hh.hostelhunter.ui.notifications;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;

public class DriveServiceHelper {
    private Drive driveService;

    public DriveServiceHelper() {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleCredential credential = null;
        try {
            credential = GoogleCredential.fromStream(new FileInputStream("path_to_your_service_account_json"))
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        driveService = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Your Application Name")
                .build();
    }

    public void uploadFile(java.io.File filePath) {
        File fileMetadata = new File();
        fileMetadata.setName(filePath.getName());
        fileMetadata.setParents(Collections.singletonList("folder_id_here"));

        FileContent mediaContent = new FileContent("image/jpeg", filePath);

        try {
            File file = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, parents")
                    .execute();

            // Change permission
            Permission permission = new Permission()
                    .setType("anyone")
                    .setRole("reader");
            driveService.permissions().create(file.getId(), permission)
                    .execute();

            // Print the view link
            String url = "https://drive.google.com/uc?export=view&id=" + file.getId();
            System.out.println("File uploaded with view URL: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}