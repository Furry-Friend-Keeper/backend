package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @GetMapping("/{keeperId}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename,@PathVariable Integer keeperId ) {
        Resource file = fileService.loadFileAsResource(filename, keeperId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
    @GetMapping("/{keeperId}/gallery/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveGalleryFile(@PathVariable String filename,@PathVariable Integer keeperId ) {
        Resource file = fileService.loadFileGallery(filename, keeperId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }

//    @PostMapping("/upload/{keeperId}")
//    public String fileUpload(@RequestParam("file") MultipartFile file,@PathVariable Integer keeperId) {
//        fileService.store(file,keeperId);
//        return "You successfully uploaded " + file.getOriginalFilename() + "!";
//    }

//    @DeleteMapping("/keeper/{keeperId}/{imgname}")
//    public String fileDelete(@PathVariable String imgname,@PathVariable Integer keeperId){
//
//        fileService.deleteProfileImg(imgname,keeperId);
//        return "delete successfully";
//    }

//    @PostMapping("/keepers-gallery/{keeperId}")
//    public ResponseEntity<List<String>> uploadGallery(@RequestParam("file") List<MultipartFile> files, @PathVariable Integer keeperId){
//
//        List<String> fileNames = fileService.storeMultiple(files,keeperId);
//        return new ResponseEntity<>(fileNames, HttpStatus.OK);
//
//
//    }
}
