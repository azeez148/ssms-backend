package com.ad.ssms.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
}
