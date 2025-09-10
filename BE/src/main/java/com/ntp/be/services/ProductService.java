package com.ntp.be.services;

import com.ntp.be.dto.ProductDto;
import com.ntp.be.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    public Product addProduct(ProductDto productDto);

    Page<ProductDto> getAllProducts(UUID categoryId, UUID typeId, Pageable pageable);

    ProductDto getProductBySlug(String slug);

    ProductDto getProductById(UUID id);

    Product updateProduct(ProductDto productDto, UUID id);

    Product fetchProductById(UUID id) throws Exception;

    void deleteProduct(UUID id);
}
