package com.ad.ssms.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryTypeService {

    @Autowired
    private DeliveryTypeRepository deliveryTypeRepository;

    public DeliveryType saveDeliveryType(DeliveryType deliveryType) {
        return deliveryTypeRepository.save(deliveryType);
    }

    public List<DeliveryType> findAllDeliveryTypes() {
        return deliveryTypeRepository.findAll();
    }

    public DeliveryType findDeliveryTypeById(int id) {
        return deliveryTypeRepository.findById(id).orElse(null);
    }
}
