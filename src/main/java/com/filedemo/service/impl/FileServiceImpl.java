package com.filedemo.service.impl;

import com.filedemo.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String storeFile(MultipartFile file) {

        try{
            File documents = new File("documents");
            if(!documents.exists()){
                documents.mkdir();
            }

            String fileName = getOriginalFileNameWithTimeStamp(file.getOriginalFilename());

            Files.copy(
                    file.getInputStream(),
                    Path.of(
                            documents.getAbsolutePath()
                                    + File.separator +
                                    fileName
                    )
            );
            return fileName;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try{
            Path filePath = Paths.get("documents"+File.separator+fileName);
            Resource resource = new UrlResource(filePath.toUri());
            return resource;
        }catch (MalformedURLException e){
            e.printStackTrace();
                return null;
        }
    }

    private static String getOriginalFileNameWithTimeStamp(String fileName){
        final int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex == -1){
            return fileName;
        }
        return String.format(
                "%s_%s%s",
                fileName.substring(0, dotIndex),
                System.currentTimeMillis(),
                fileName.substring(dotIndex)
        );
    }

}
