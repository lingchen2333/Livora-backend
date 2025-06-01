package com.lingchen.buynow.service.category;

import com.lingchen.buynow.entity.Category;
import com.lingchen.buynow.entity.Product;
import com.lingchen.buynow.repository.CategoryRepository;
import com.lingchen.buynow.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Category addCategory(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            throw new EntityExistsException(categoryName + " already exists!");
        }
        return categoryRepository.save(new Category(categoryName));
    }


    @Override
    public Category updateCategory(String newCategoryName, Long categoryId) {
        Category oldCategory = this.getCategoryById(categoryId);
        oldCategory.setName(newCategoryName);
        return categoryRepository.save(oldCategory);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category categoryToDelete = this.getCategoryById(id);
        List<Product> productsAffected = productRepository.findByCategoryName(categoryToDelete.getName());
        productsAffected.forEach(product -> {product.setCategory(null);});
        categoryRepository.delete(categoryToDelete);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElse(null);
    }
}
