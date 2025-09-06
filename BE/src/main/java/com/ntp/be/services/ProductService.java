package com.ntp.be.services;

import com.ntp.be.dto.ProductDto;
import com.ntp.be.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    public Product addProduct(ProductDto productDto);

    public List<ProductDto> getAllProducts(UUID categoryId, UUID typeId);

    ProductDto getProductBySlug(String slug);

    ProductDto getProductById(UUID id);

    Product updateProduct(ProductDto productDto, UUID id);

    Product fetchProductById(UUID id) throws Exception;

    void deleteProduct(UUID id);
}
