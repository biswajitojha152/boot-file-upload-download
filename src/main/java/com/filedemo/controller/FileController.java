package com.filedemo.controller;

import com.filedemo.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
            String fileName = fileService.storeFile(file);

            return ResponseEntity.ok(fileName);
    }

    @GetMapping(value = "/downloadFile/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileName") String fileName, HttpServletRequest request){
        Resource fileResource = fileService.loadFileAsResource(fileName);

        String contentType = null;
        try{
            contentType = request.getServletContext()
                    .getMimeType(
                            fileResource.getFile().getAbsolutePath()
                    );
        }catch (IOException e){
            e.printStackTrace();
        }

        if(contentType == null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }
}
