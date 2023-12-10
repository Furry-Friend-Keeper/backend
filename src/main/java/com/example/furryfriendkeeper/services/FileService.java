package com.example.furryfriendkeeper.services;

import com.example.furryfriendkeeper.properties.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }

    }

    public String store(MultipartFile file,Integer keeperId) {
//        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
//        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
//        String fileName = keeperId.toString() + ".jpg";
          String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(keeperId.toString()).resolve(fileName);
            Files.createDirectories(targetLocation.getParent());

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }


    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName);
            Files.delete(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + fileName + ". Please try again!", ex);
        }
    }
    public List<String> storeMultiple(List<MultipartFile> files,Integer keeperId) {
        List<String> fileNames = new ArrayList<>();

        for (MultipartFile file : files) {
//            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = StringUtils.getFilenameExtension(originalFileName);
            String newFileName = UUID.randomUUID().toString() + "." + fileExtension;


//            try {
//                if (fileName.contains("..")) {
//                    throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
//                }
//
//                Path targetLocation = this.fileStorageLocation.resolve(fileName);
//                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//                fileNames.add(fileName);
//            } catch (IOException ex) {
//                throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
//            }
            try {
                Path targetLocation = this.fileStorageLocation.resolve(keeperId.toString()).resolve("gallery").resolve(newFileName);
                Files.createDirectories(targetLocation.getParent());

                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                fileNames.add(newFileName);
            } catch (IOException ex) {
                throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
            }
        }
        return fileNames;
    }


}
