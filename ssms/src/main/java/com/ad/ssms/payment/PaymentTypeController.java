package com.ad.ssms.payment;

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
@RequestMapping("/paymentType")
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;

    @PostMapping("/addPaymentType")
    public PaymentType addCategory(@RequestBody PaymentType paymentType) {
        return paymentTypeService.savePaymentType(paymentType);
    }

    @GetMapping("/all")
    public List<PaymentType> getAllPaymentTypes() {
        return paymentTypeService.findAllPaymentTypes();
    }
}
