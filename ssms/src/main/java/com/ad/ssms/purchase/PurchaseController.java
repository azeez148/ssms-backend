package com.ad.ssms.purchase;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200") // Replace with your allowed origins
@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/addPurchase")
    public Purchase addPurchase(@RequestBody Purchase purchase) {
        return purchaseService.savePurchase(purchase);
    }

    @GetMapping("/all")
    public List<Purchase> getAllPurchases() {
        return purchaseService.findAllPurchases();
    }
}