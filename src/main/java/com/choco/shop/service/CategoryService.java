package com.choco.shop.service;

import com.choco.shop.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findById(Long id);

    Category save(Category category);

    void deleteById(Long id);
}
