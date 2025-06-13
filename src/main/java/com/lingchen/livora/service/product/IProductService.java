package com.lingchen.livora.service.product;

import com.lingchen.livora.dto.ProductDto;
import com.lingchen.livora.entity.Product;
import com.lingchen.livora.request.AddProductRequest;
import com.lingchen.livora.request.UpdateProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    List<Product> searchProductByImage(MultipartFile image) throws IOException;

    List<String> getDistinctProductBrands();

    ProductDto convertToDto(Product product);
    List<ProductDto> getConvertedProducts(List<Product> products);
}
