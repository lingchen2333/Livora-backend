package com.lingchen.livora.service.product;

import com.lingchen.livora.dto.ImageDto;
import com.lingchen.livora.dto.ProductDto;
import com.lingchen.livora.entity.*;
import com.lingchen.livora.repository.*;
import com.lingchen.livora.request.AddProductRequest;
import com.lingchen.livora.request.UpdateProductRequest;
import com.lingchen.livora.service.chroma.IChromaService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final IChromaService chromaService;

    @Override
    public Product addProduct(AddProductRequest request) {
        if (productExists(request.getName(), request.getBrand())) {
            throw new EntityExistsException(request.getName() + " already exists");
        }
        String categoryName = request.getCategoryName();
        Category category = (categoryRepository.findByName(categoryName))
                .orElseGet(() -> categoryRepository.save(new Category(categoryName)));

        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        String categoryName = request.getCategoryName();
        Category category = categoryRepository.findByName(categoryName)
                .orElseGet(() -> categoryRepository.save(new Category(categoryName)));

        existingProduct.setCategory(category);
        return productRepository.save(existingProduct);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(product -> {

                            //relationship between product and cartItem
                            List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
                            //cartItems & cart
                            cartItems.forEach(cartItem -> {
                                Cart cart = cartItem.getCart();
                                cart.removeItem(cartItem);
                                //cartItemRepository.delete(cartItem);
                            });

                            //relationship between product and orderItem
                            List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
                            orderItems.forEach(orderItem -> {
                                //order item without any product still exist in order
                                orderItem.setProduct(null);
                                orderItemRepository.save(orderItem);
                            });

                            //product & category
                            Optional.ofNullable(product.getCategory())
                                    .ifPresent(category -> {
                                        category.getProducts().remove(product);
                                    });
                            //product.setCategory(null);

                            //delete product
                            productRepository.deleteById(productId);
                        }, () ->
                        {
                            throw new EntityNotFoundException("Product not found");
                        }
                );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getFirstProductPerDistinctName() {
        List<Product> products = this.getAllProducts();
        Map<String, Product> distinctNamedProductMap = products.stream()
                .collect(Collectors.toMap(
                        Product::getName, //key mapper function
                        product -> product, //value mapper
                        (oldValue, newValue) -> oldValue //merge function
                ));
        return new ArrayList<>(distinctNamedProductMap.values());
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }


    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> searchProductByImage(MultipartFile image) throws IOException {
        Set<Long> productIds = chromaService.searchImageSimilarity(image);
        return productRepository.findAllById(productIds);
    }

    @Override
    public List<String> getDistinctProductBrands() {
        return this.getAllProducts()
                .stream()
                .map(Product::getBrand)
                .distinct()
                .toList();
    }


    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;

    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }


}
