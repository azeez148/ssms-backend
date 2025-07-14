/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ad.ssms.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.product.ProductService;

@Service
class HomeService {
    // This service class would typically interact with the repository layer to fetch data
    // For now, we can just return a dummy Dashboard object

    @Autowired
    private ProductService productService;

    public Home getHomeData() {
        Home home = new Home();        
        // get all products using product service
        home.setProducts(productService.findAllProducts());
        // Add more data as needed
        return home;
    }
}
