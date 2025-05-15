package com.ad.ssms.stock;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ad.ssms.category.Category;
import com.ad.ssms.category.CategoryService;
import com.ad.ssms.product.Product;
import com.ad.ssms.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/stock")
public class StockController {
    
    // Inject your product service (assumes a service that can persist Product)
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/uploadExcel")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select an Excel file to upload.");
        }
        try {
            // Process the Excel data using Apache POI and map each row to a Product model.
            try (InputStream is = file.getInputStream();
                 Workbook workbook = WorkbookFactory.create(is)) {
                 
                Sheet sheet = workbook.getSheetAt(0);
                boolean isHeader = true;
                // Iterate over each row
                for (Row row : sheet) {
                    // Skip header row
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }

                    // Read cells in order: name, description, category, sizeMap, unitPrice, sellingPrice
                    String name = getCellValueAsString(row.getCell(0));
                    String description = getCellValueAsString(row.getCell(1));
                    String categoryName = getCellValueAsString(row.getCell(2));
                    String sizeMapString = getCellValueAsString(row.getCell(3));
                    double unitPrice = getCellValueAsDouble(row.getCell(4));
                    double sellingPrice = getCellValueAsDouble(row.getCell(5));


                    // Convert to valid JSON: Add double quotes to keys
                    String jsonString = sizeMapString.replaceAll("([a-zA-Z0-9]+):", "\"$1\":");

                    // Parse JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Integer> sizeMap = objectMapper.readValue(jsonString, Map.class);
                    // Map to Product model (assumes a no-args constructor & setters)
                    Product product = new Product();
                    product.setName(name);
                    product.setDescription(description);
                    Category category = categoryService.findCategoryByName(categoryName);
                    product.setCategory(category);

                    product.setSizeMap(sizeMap);
                    product.setUnitPrice((int) unitPrice);
                    product.setSellingPrice((int) sellingPrice);

                    // Call the product create API via your service to persist the product.
                    productService.createProduct(product);
                }
            }
            
            // Define the directory to store uploaded Excel files.
            Path uploadDir = Paths.get("src/main/resources/uploadedExcels");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            // Save the Excel file for record keeping.
            Path targetLocation = uploadDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // Respond with a JSON message.
            Map<String, String> responseMsg = new HashMap<>();
            responseMsg.put("message", "Excel uploaded and processed successfully.");
            return ResponseEntity.ok(responseMsg);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload/process the file: " + file.getOriginalFilename());
        }
    }

    // Helper method to safely convert a cell value to String.
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    // Helper method to safely convert a cell value to double.
    private double getCellValueAsDouble(Cell cell) {
        if (cell == null) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else {
            try {
                return Double.parseDouble(getCellValueAsString(cell));
            } catch (NumberFormatException ex) {
                return 0.0;
            }
        }
    }
}