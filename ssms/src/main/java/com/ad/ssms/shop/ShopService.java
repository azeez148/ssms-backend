package com.ad.ssms.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    public Shop saveShop(Shop shop) {
        return shopRepository.save(shop);
    }

    public List<Shop> findAllShops() {
        return shopRepository.findAll();
    }
}
