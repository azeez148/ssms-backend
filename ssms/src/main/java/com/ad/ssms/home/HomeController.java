package com.ad.ssms.home;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:4200") // Replace with your allowed origins
@RestController
@RequestMapping("/public")
public class HomeController {
    
    @Autowired
    private HomeService homeService;
    
    @GetMapping("/all")
    public Home getHomeData() {
        return homeService.getHomeData();
    }

    @GetMapping("/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        try {
            Path imagesDir = Paths.get("src/main/resources/images/products/" + productId);
            Path imageFile = null;
            if (Files.exists(imagesDir)) {
                List<Path> imageFiles = Files.list(imagesDir)
                                             .filter(Files::isRegularFile)
                                             .collect(Collectors.toList());
                if (!imageFiles.isEmpty()) {
                    imageFile = imageFiles.get(0);
                }
            }

            if (imageFile == null) {
                // Return the default notfound.png image if the product image is not available
                Path notFoundImage = Paths.get("src/main/resources/images/notfound.png");
                if (!Files.exists(notFoundImage)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                byte[] imageBytes = Files.readAllBytes(notFoundImage);
                String mimeType = Files.probeContentType(notFoundImage);
                return ResponseEntity.ok()
                                     .header("Content-Type", mimeType)
                                     .body(imageBytes);
            } else {
                byte[] imageBytes = Files.readAllBytes(imageFile);
                String mimeType = Files.probeContentType(imageFile);
                return ResponseEntity.ok()
                                     .header("Content-Type", mimeType)
                                     .body(imageBytes);
            }
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
