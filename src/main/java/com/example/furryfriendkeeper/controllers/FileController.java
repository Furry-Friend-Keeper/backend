package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
import com.example.furryfriendkeeper.repositories.UserRepository;
import com.example.furryfriendkeeper.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private final FileService fileService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;


    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @GetMapping("/{keeperId}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename,@PathVariable Integer keeperId ) {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Resource file = fileService.loadFileAsResource(filename, keeperId,role);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
    @GetMapping("/{keeperId}/gallery/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveGalleryFile(@PathVariable String filename,@PathVariable Integer keeperId ) {
        Resource file = fileService.loadFileGallery(filename, keeperId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }


}
