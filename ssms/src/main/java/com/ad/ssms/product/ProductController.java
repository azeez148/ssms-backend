package com.ad.ssms.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ad.ssms.category.Category;
import com.ad.ssms.category.CategoryService;

@CrossOrigin(origins = "http://localhost:4200") // Replace with your allowed origins
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/addProduct")
    public Product addProduct(@RequestBody ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setUnitPrice(productRequest.getUnitPrice());
        product.setSellingPrice(productRequest.getSellingPrice());
        Category category = categoryService.findCategoryById(productRequest.getCategoryId());
        product.setCategory(category);
        return productService.saveProduct(product);
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }



    /**
     * Update sizeMap for a product.
     * 
     * @param productId The ID of the product.
     * @param sizeMap The new sizeMap to update.
     * @return Updated product.
     */
    @PostMapping("/updateSizeMap")
    public Product updateSizeMap(@RequestBody UpdateSizeMapRequest updateRequest) {
        // Find product by ID
        Product product = productService.findProductById(updateRequest.getProductId());

        // Check category of the product
        // Category category = product.getCategory();

        Map<String, Integer> updatedSizeMap = updateRequest.getSizeMap();
        // Custom logic for jersey category sizeMap
        product.setSizeMap(updatedSizeMap);
        
        // Add category-specific logic here, e.g., for "jersey" category
        // if (category != null && "jersey".equalsIgnoreCase(category.getName())) {
        //     // Apply specific logic for jersey sizeMap (e.g., modify the sizeMap based on your needs)
        //     // For example, jerseys may only allow specific sizes (S, M, L, XL, etc.)
        //     Map<String, Integer> updatedSizeMap = updateRequest.getSizeMap();
        //     // Custom logic for jersey category sizeMap
        //     product.setSizeMap(updatedSizeMap);
        // } else {
        //     // Handle generic categories
        //     product.setSizeMap(updateRequest.getSizeMap());
        // }

        return productService.saveProduct(product);
    }

    @PostMapping("/filterProducts")
    public List<Product> getFilteredProducts(@RequestBody ProductFilterRequest filterRequest) {
        return productService.getFilteredProducts(filterRequest.getCategoryId(), filterRequest.getProductTypeFilter());
    }

    @PostMapping("/upload-images")
    public ResponseEntity<?> uploadProductImages(@RequestParam("productId") Long productId,
                                                   @RequestParam("images") List<MultipartFile> images) {
        Product product = productService.findProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        
        for (MultipartFile image : images) {
            try {
                // Implement file storage logic here.
                // For example, save the file to disk or cloud storage and update the product image list.
                // String fileName = image.getOriginalFilename();
                // Path targetLocation = Paths.get("your/upload/directory").resolve(fileName);
                // Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                Path imagesDir = Paths.get("src/main/resources/images/products/" + product.getId());
                if (!Files.exists(imagesDir)) {
                    Files.createDirectories(imagesDir);
                }

                Path targetLocation = imagesDir.resolve(image.getOriginalFilename());
                Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Error uploading image IOException: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Error uploading image: " + image.getOriginalFilename());
            }
            catch (Exception e) {
                System.out.println("Error uploading image Exception: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Error uploading image: " + image.getOriginalFilename());
            }
        }
        
        // Optionally update and save the product if image information is stored.
        // productService.saveProduct(product);
        
        return ResponseEntity.ok("Images uploaded successfully");
    }


    // update-whatsapp-group implement the update-whatsapp-group API which will send a new message to the whataspp group
    // @PostMapping("/update-whatsapp-group")
    // public ResponseEntity<?> updateWhatsappGroup(@RequestBody WhatsappGroupRequest whatsappGroupRequest) {
    //     // Implement the logic to send a message to the WhatsApp group
    //     // For example, you can use a third-party library or service to send the message
    //     // You can also log the message to a file or database for tracking purposes
    //     // suggest a best way to send message to whatsapp group
        
    //     System.out.println("Sending message to WhatsApp group: " + whatsappGroupRequest.getMessage());
    //     return ResponseEntity.ok("Message sent to WhatsApp group successfully");
    // }

    @GetMapping("/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        try {
            Path imagesDir = Paths.get("src/main/resources/images/products/" + productId);
            if (!Files.exists(imagesDir)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Retrieve the first image file in the directory
            List<Path> imageFiles = Files.list(imagesDir)
                                         .filter(Files::isRegularFile)
                                         .collect(Collectors.toList());
            if (imageFiles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            
            Path imageFile = imageFiles.get(0);
            byte[] imageBytes = Files.readAllBytes(imageFile);
            String mimeType = Files.probeContentType(imageFile);
            
            return ResponseEntity.ok()
                                 .header("Content-Type", mimeType)
                                 .body(imageBytes);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

