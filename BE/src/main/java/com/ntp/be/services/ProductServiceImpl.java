package com.ntp.be.services;

import com.ntp.be.dto.ProductDto;
import com.ntp.be.entities.Product;
import com.ntp.be.exception.DuplicateException;
import com.ntp.be.exception.ResourceNotFoundException;
import com.ntp.be.mapper.ProductMapper;
import com.ntp.be.repositories.ProductRepository;
import com.ntp.be.specification.ProductSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product addProduct(ProductDto productDto) {
        boolean exists = productRepository.existsByName(productDto.getName());

        if (exists) {
            throw new DuplicateException("Product with name " + productDto.getName() + " already exists");
        }

        Product product = productMapper.mapToProductEntity(productDto);
        return productRepository.save(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(UUID categoryId, UUID typeId, Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> cb.conjunction();
        if (categoryId != null) {
            spec = spec.and(ProductSpecification.hasCategoryId(categoryId));
        }
        if (typeId != null) {
            spec = spec.and(ProductSpecification.hasCategoryTypeId(typeId));
        }

        Page<Product> pageResult = productRepository.findAll(spec, pageable);
        return pageResult.map(productMapper::mapProductToDto);
    }

    @Override
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug);

        if (product == null) {
            throw new ResourceNotFoundException("Product with Slug " + slug + " not found");
        }

        ProductDto productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        productDto.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return productDto;
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        ProductDto productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        productDto.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));

        return productDto;
    }

    @Override
    public Product updateProduct(ProductDto productDto, UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        productDto.setId(product.getId());
        return productRepository.save(productMapper.mapToProductEntity(productDto));
    }

    @Override
    public Product fetchProductById(UUID id) throws Exception {
        return productRepository.findById(id).orElseThrow(BadRequestException::new);
    }

    @Override
    public void deleteProduct(UUID productId) {
        boolean exists = productRepository.existsById(productId);

        if (!exists) {
            throw new ResourceNotFoundException("Product with id " + productId + " not found");
        }

        productRepository.deleteById(productId);
    }

}
