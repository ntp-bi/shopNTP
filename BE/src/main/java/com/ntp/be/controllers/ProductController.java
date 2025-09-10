package com.ntp.be.controllers;

import com.ntp.be.dto.ProductDto;
import com.ntp.be.entities.Product;
import com.ntp.be.services.ProductService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/ntpshop")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(required = false, name = "categoryId", value = "categoryId") UUID categoryId,
                                                           @RequestParam(required = false, name = "typeId", value = "typeId") UUID typeId,
                                                           @RequestParam(required = false) String slug,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        if (StringUtils.isNotBlank(slug)) {
            ProductDto productDto = productService.getProductBySlug(slug);
            return new ResponseEntity<>(Page.empty().map(x -> productDto), HttpStatus.OK);
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDto> productList = productService.getAllProducts(categoryId, typeId, pageable);
            return new ResponseEntity<>(productList, HttpStatus.OK);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        ProductDto productDto = productService.getProductById(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        try {
            Product product = productService.addProduct(productDto);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productDto, @PathVariable(value = "id", required = true) UUID id) {
        Product product = productService.updateProduct(productDto, id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id", required = true) UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
