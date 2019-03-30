package com.imhungry.backend.controller;

import com.imhungry.backend.CollageBuilder;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by calebthomas on 3/5/19.
 */
@RestController
@CrossOrigin
@RequestMapping("/collage")
public class CollageController {

    @Autowired
    private CollageBuilder collageBuilder;

    @GetMapping
    public List<URL> getCollage(@RequestParam(value = "searchTerm") String searchTerm) throws IOException {
        List<URL> imageUrls = collageBuilder.getUrls(searchTerm + " food", 10);
        return imageUrls;
    }
}
