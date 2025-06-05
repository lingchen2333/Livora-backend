package com.lingchen.livora.controller;


import com.lingchen.livora.entity.Category;
import com.lingchen.livora.response.ApiResponse;
import com.lingchen.livora.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse("Found", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Success", category));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCategory(@RequestBody String categoryName) {
        Category addedCategory = categoryService.addCategory(categoryName);
        return ResponseEntity.ok(new ApiResponse("Success", addedCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody String newCategoryName) {
        Category updatedCategory = categoryService.updateCategory(newCategoryName, id);
        return ResponseEntity.ok(new ApiResponse("Update success!", updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Deletion successful", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getCategoryByName(@RequestParam String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(new ApiResponse("Success", category));
    }
}
