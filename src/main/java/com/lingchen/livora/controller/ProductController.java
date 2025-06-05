package com.lingchen.livora.controller;


import com.lingchen.livora.dto.ProductDto;
import com.lingchen.livora.entity.Product;
import com.lingchen.livora.request.AddProductRequest;
import com.lingchen.livora.request.UpdateProductRequest;
import com.lingchen.livora.response.ApiResponse;
import com.lingchen.livora.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request) {
        Product product = productService.addProduct(request);
        ProductDto productDto = productService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Product added successfully", productDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long productId) {
        Product product = productService.updateProduct(request, productId);
        ProductDto productDto = productService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Product updated successfully", productDto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        ProductDto productDto = productService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Product get successfully", productDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully!", productId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Get all products successfully!", convertedProducts));
    }

    @GetMapping("/distinct")
    public ResponseEntity<ApiResponse> getFirstProductPerDistinctName() {
        List<Product> products = productService.getFirstProductPerDistinctName();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Get products with distinct name successfully!", convertedProducts));
    }

    @GetMapping("/distinct/brands")
    public ResponseEntity<ApiResponse> getDistinctProductBrands() {
        List<String> brands = productService.getDistinctProductBrands();
        return ResponseEntity.ok(new ApiResponse("Get distinct product brands successfully!", brands));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String name) {

        List<Product> products;
        if (category != null && brand != null) {
            products = productService.getProductsByCategoryAndBrand(category, brand);
        } else if (brand != null && name != null) {
            products = productService.getProductsByBrandAndName(brand, name);
        } else if (category != null) {
            products = productService.getProductsByCategory(category);
        } else if (brand != null) {
            products = productService.getProductsByBrand(brand);
        } else if (name != null) {
            products = productService.getProductsByName(name);
        } else {
            products = productService.getAllProducts();
        }

        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products retrieved successfully!", convertedProducts));
    }
}
