package com.imhungry.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by calebthomas on 3/5/19.
 */
public class CollageBuilder {

    public final int MAX_ROTATION = 45;
    public final int MIN_ROTATION = -45;

    @Value("${engine.id}")
    private String ENGINE_ID;

    @Value("${search.api.key}")
    private String SEARCH_API_KEY;

    public BufferedImage buildCollage(List<URL> imageUrls, boolean rotate, int height, int width) throws IOException {
        // load images from URLs
        List<BufferedImage> images = new ArrayList<>();
        for(URL url: imageUrls) {
            BufferedImage image = ImageIO.read(url);
            images.add(image);
        }

        // calculate area remaining in generated collage
        int area = height * width / 20 * images.size();
        int numRows = (int) Math.sqrt(images.size());
        int rowSpacing = height / (numRows +1);
        int columnSpacing = width / (numRows +1);

        // Build collage canvas
        BufferedImage collageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = collageBuffer.createGraphics();

        // Create the background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Randomly layout images
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        for (Integer i : indices){
            BufferedImage currentImage = images.get(i);
            // Lay out images in numRows rows and numRows columns
            currentImage = resizeToArea(currentImage, area/(images.size()));
            if(rotate) {
                currentImage = randomRotate(currentImage, MIN_ROTATION, MAX_ROTATION);
            }
            int row = i / numRows;
            int col = i % numRows;
            // add image to canvas
            int x = col*columnSpacing;
            int y = row*rowSpacing;
            g2d.drawImage(currentImage, null, x, y);
        }

        g2d.dispose();

        return collageBuffer;
    }

    private BufferedImage resizeToArea(BufferedImage image, double area) {
        // Calculate scaling factor
        int width = image.getWidth();
        int height = image.getHeight();
        double originalArea = width * height;
        double scaleFactor = Math.sqrt(area / originalArea);

        // Create scaled image
        BufferedImage scaledImage = new BufferedImage((int) (scaleFactor * width),
                (int) (scaleFactor * height),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImage.createGraphics();

        // Apply transform
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.scale(scaleFactor, scaleFactor);
        g2d.drawRenderedImage(image, scaleTransform);
        g2d.dispose();

        return scaledImage;
    }

    private BufferedImage randomRotate(BufferedImage image, int minRotation, int maxRotation) {
        // Choose random angle
        double angleDegrees = Math.random() * Math.abs(maxRotation - minRotation) + minRotation;
        double angleRadians = angleDegrees * (Math.PI / 180.0);

        double sin = Math.abs(Math.sin(angleRadians));
        double cos = Math.abs(Math.cos(angleRadians));

        // Calculate new width and height
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int newWidth = (int) (originalWidth * cos + originalHeight * sin);
        int newHeight = (int) (originalWidth * sin + originalHeight * cos);

        // Create new image
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, newWidth, newHeight);

        // rotate image
        g2d.translate((newWidth - originalWidth) / 2, (newHeight - originalHeight) / 2);
        g2d.rotate(angleRadians, originalWidth / 2, originalHeight / 2);
        g2d.drawRenderedImage(image, null);

        return rotatedImage;
    }


    public List<URL> getUrls(String searchTerm, int numImages) throws IOException {
        List<URL> imageUrls = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        HttpUrl requestUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("www.googleapis.com")
                .addPathSegment("customsearch")
                .addPathSegment("v1")
                .addQueryParameter("key", SEARCH_API_KEY)
                .addQueryParameter("cx", ENGINE_ID)
                .addQueryParameter("q", searchTerm)
                .addQueryParameter("fields", "items/image/thumbnailLink")
                .addQueryParameter("searchType", "image")
                .addQueryParameter("num", "10")
                .build();

        // Loop requests until we get desired images
        int resultsIndex = 1;
        while (imageUrls.size() < numImages) {
            // Build request
            HttpUrl pageRequestUrl = HttpUrl.parse(requestUrl.toString()).newBuilder()
                    .addQueryParameter("start", Integer.toString(resultsIndex))
                    .build();

            Request request = new Request.Builder()
                    .url(pageRequestUrl)
                    .get()
                    .build();

            // Execute request
            Response res = client.newCall(request).execute();

            // Parse JSON response
            JsonObject myResponse = new JsonParser().parse(res.body().charStream()).getAsJsonObject();
            JsonArray items = myResponse.getAsJsonArray("items");

            // loop through the images
            for (JsonElement item : items){
                JsonObject image = item.getAsJsonObject().getAsJsonObject("image");

                // Get image thumbnail URL
                URL link = new URL(image.get("thumbnailLink").getAsString());
                imageUrls.add(link);

                // finishes once images are downloaded
                if (imageUrls.size() == numImages) {
                    break;
                }
            }

            // For multiple google custom search requests
            resultsIndex += 10;
        }

        return imageUrls;
    }

}
