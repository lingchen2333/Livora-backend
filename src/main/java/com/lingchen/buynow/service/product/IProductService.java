package com.lingchen.buynow.service.product;

import com.lingchen.buynow.dto.ProductDto;
import com.lingchen.buynow.entity.Product;
import com.lingchen.buynow.request.AddProductRequest;
import com.lingchen.buynow.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest request);
    Product updateProduct(UpdateProductRequest request, Long productId);
    Product getProductById(Long productId);
    void deleteProductById(Long productId);

    List<Product> getAllProducts();
    List<Product> getFirstProductPerDistinctName();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<String> getDistinctProductBrands();

    ProductDto convertToDto(Product product);
    List<ProductDto> getConvertedProducts(List<Product> products);
}
