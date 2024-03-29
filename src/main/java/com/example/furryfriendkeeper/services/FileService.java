package com.example.furryfriendkeeper.services;

import com.example.furryfriendkeeper.entities.Gallery;
import com.example.furryfriendkeeper.entities.Petkeepers;
import com.example.furryfriendkeeper.properties.FileStorageProperties;
import com.example.furryfriendkeeper.repositories.GalleryRepository;
import com.example.furryfriendkeeper.repositories.PetkeeperRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


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
    private ModelMapper modelMapper;
    @Autowired
    private PetkeeperRepository petkeeperRepository;

    @Autowired
    private GalleryRepository galleryRepository;

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

    public String store(MultipartFile file,Integer userId,String role) {

          String fileName = StringUtils.cleanPath(file.getOriginalFilename());
          String contentType = file.getContentType();

          if(isSupportedContentType(contentType)){
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if(role.equals("PetKeeper")) {
                Path targetLocation = this.fileStorageLocation.resolve(userId.toString()).resolve(fileName);
                Files.createDirectories(targetLocation.getParent());
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            if(role.equals("Owner")){
                Path targetLocation = this.fileStorageLocation.resolve("Owner").resolve(userId.toString()).resolve(fileName);
                Files.createDirectories(targetLocation.getParent());
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
          }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid file typeInvalid file type(jpg,png and jpeg only),please try again");

    }



    public Resource loadFileAsResource(String fileName,Integer userId,String role) {
        try {
            Path filePath = null;
            Resource resource = null;
            if(role.equals("PetKeeper")){
                filePath = this.fileStorageLocation.resolve(userId.toString()).resolve(fileName).normalize();
                resource = new UrlResource(filePath.toUri());
            }
            if(role.equals("Owner")){
                filePath = this.fileStorageLocation.resolve("Owner").resolve(userId.toString()).resolve(fileName).normalize();
                resource = new UrlResource(filePath.toUri());
            }
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"File not found " + fileName);
        }
    }
    public Resource loadFileGallery(String fileName,Integer keeperId) {
        try {
            Path filePath = this.fileStorageLocation.resolve(keeperId.toString()).resolve("gallery").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"File not found " + fileName);
        }
    }



    public void deleteProfileImg(String fileName,Integer userId,String role) {
        try {
            if(role.equals("PetKeeper")) {
                Path filePath = this.fileStorageLocation.resolve(userId.toString()).resolve(fileName);
                Files.delete(filePath);
            }
            if(role.equals("Owner")){
                Path filePath = this.fileStorageLocation.resolve("Owner").resolve(userId.toString()).resolve(fileName);
                Files.delete(filePath);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + fileName + ". Please try again!", ex);
        }
    }
    public void deleteGallery(List<String> fileNames,Integer keeperId) {
        try {
            for (String fileName : fileNames) {
                Path filePath = this.fileStorageLocation.resolve(keeperId.toString()).resolve("gallery").resolve(fileName);
                Files.delete(filePath);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete files. Please try again!", ex);
        }
    }
    public List<String> storeMultiple(List<MultipartFile> files,Integer keeperId) {
        List<String> fileNames = new ArrayList<>();
        Petkeepers petkeeper = modelMapper.map(petkeeperRepository.findById(keeperId),Petkeepers.class);

            for (MultipartFile file : files) {
                String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
                String fileExtension = StringUtils.getFilenameExtension(originalFileName);
                String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
                String contentType = file.getContentType();
                if(isSupportedContentType(contentType)) {
                    try {
                        Path targetLocation = this.fileStorageLocation.resolve(keeperId.toString()).resolve("gallery").resolve(newFileName);
                        Files.createDirectories(targetLocation.getParent());

                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


                        Gallery gallery = new Gallery();
                        gallery.setPetKeeper(petkeeper);
                        gallery.setGallery(newFileName);
                        galleryRepository.saveAndFlush(gallery);

                        fileNames.add(newFileName);

                    } catch (IOException ex) {
                        throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
                    }
                }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid file type(jpg,png and jpeg only),please try again");
            }

        return fileNames;
    }
    public boolean doesImageExist(String fileName, Integer userId, String role) {
        try {
            Path filePath = null;
            if (role.equals("PetKeeper")) {
               filePath = this.fileStorageLocation.resolve(userId.toString()).resolve(fileName).normalize();
            }
            if(role.equals("Owner")){
               filePath = this.fileStorageLocation.resolve("Owner").resolve(userId.toString()).resolve(fileName).normalize();
            }
            return Files.exists(filePath) && Files.isRegularFile(filePath);
        } catch (Exception ex) {
            throw new RuntimeException("Error checking file existence", ex);
        }
    }

    public boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }

}
