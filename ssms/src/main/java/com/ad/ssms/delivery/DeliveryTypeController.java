package com.ad.ssms.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // Replace with your allowed origins
@RestController
@RequestMapping("/deliveryType")
public class DeliveryTypeController {

    @Autowired
    private DeliveryTypeService deliveryTypeService;

    @PostMapping("/addDeliveryType")
    public DeliveryType addCategory(@RequestBody DeliveryType deliveryType) {
        return deliveryTypeService.saveDeliveryType(deliveryType);
    }

    @GetMapping("/all")
    public List<DeliveryType> getAllPaymentTypes() {
        return deliveryTypeService.findAllDeliveryTypes();
    }
}
