package com.imhungry.backend.controller;

import com.imhungry.backend.CollageBuilder;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getCollage(@RequestParam(value = "searchTerm", defaultValue = "chinese") String searchTerm) throws IOException {
        List<URL> imageUrls = collageBuilder.getUrls(searchTerm + " food", 10);

        BufferedImage collageImage = collageBuilder.buildCollage(imageUrls, 400, 600);
        ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        ImageIO.write(collageImage, "jpg", imageStream);

        return imageStream.toByteArray();
    }
}