package com.ad.ssms.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentTypeService {

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    public PaymentType savePaymentType(PaymentType paymentType) {
        return paymentTypeRepository.save(paymentType);
    }

    public List<PaymentType> findAllPaymentTypes() {
        return paymentTypeRepository.findAll();
    }

    public PaymentType findPaymentTypeById(int id) {
        return paymentTypeRepository.findById(id).orElse(null);
    }
}
