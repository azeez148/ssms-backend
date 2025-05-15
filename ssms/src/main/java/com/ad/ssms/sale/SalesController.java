package com.ad.ssms.sale;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/addSale")
    public Sale addSale(@RequestBody Sale sale) {
        return saleService.saveSale(sale);
    }

    @GetMapping("/all")
    public List<Sale> getAllSales() {
        return saleService.findAllSales();
    }
}