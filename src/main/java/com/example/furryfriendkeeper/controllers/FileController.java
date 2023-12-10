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
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileService.loadFileAsResource(filename);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
    @PostMapping("/keeperprofile")
    public String fileUpload(@RequestParam("file") MultipartFile file) {
        Integer id = null;
        fileService.store(file,id);
        return "You successfully uploaded " + file.getOriginalFilename() + "!";
    }

    @DeleteMapping("/keeperprofile/{id}")
    public String fileDelete(@PathVariable String id){
        fileService.deleteFile(id);
        return "delete successfully";
    }

    @PostMapping("/keepers-gallery")
    public ResponseEntity<List<String>> uploadGallery(@RequestParam("file") List<MultipartFile> files){
        Integer id = 2;
        List<String> fileNames = fileService.storeMultiple(files,id);
        return new ResponseEntity<>(fileNames, HttpStatus.OK);
    }
}
