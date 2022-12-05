package io.github.ntask_api.web.rest;

import io.github.ntask_api.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UploadCloud {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/public/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        Long str = System.currentTimeMillis();
        String url = cloudinaryService.uploadFile(file);
        System.out.println(System.currentTimeMillis()-str);
        return url;
    }
}
