package com.imhungry.backend.controller;

import com.imhungry.backend.sourcer.CollageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/collage")
public class CollageController {

    @Autowired
    private CollageBuilder collageBuilder;

    @GetMapping
    public List<URL> getCollage(@RequestParam(value = "searchTerm", defaultValue = "") String searchTerm) throws IOException {
        return collageBuilder.getUrls(searchTerm + " food", 10);
    }
}