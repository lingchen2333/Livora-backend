package com.lingchen.buynow.service.category;

import com.lingchen.buynow.entity.Category;

import java.util.List;

public interface ICategoryService {

    Category addCategory(Category category);
    Category updateCategory(Category newCategory, Long categoryId);
    Category getCategoryById(Long id);
    void deleteCategoryById(Long id);

    List<Category> getAllCategories();
    Category getCategoryByName(String name);

}
