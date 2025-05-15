// filepath: /c:/Projects/ADrenaline/ssms/src/main/java/com/ad/ssms/attribute/AttributeService.java
package com.ad.ssms.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.category.Category;
import com.ad.ssms.category.CategoryRepository;

import java.util.List;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    public Attribute saveAttribute(Attribute attribute) {
        return attributeRepository.save(attribute);
    }

    public List<Attribute> findAllAttributes() {
        return attributeRepository.findAll();
    }

    public Category findCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
