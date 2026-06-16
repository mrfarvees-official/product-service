package org.groupf.productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.groupf.productservice.dto.ProductCreateRequest;
import org.groupf.productservice.dto.ProductUpdateRequest;
import org.groupf.productservice.entity.Product;
import org.groupf.productservice.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping
    public Product createProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product product = new Product();

        product.setName(request.name());
        product.setUnitPrice(request.unitPrice());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setStock(request.stock());

        return productRepository.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return null;
        }

        if (request.name() != null) product.setName(request.name());
        if (request.unitPrice() != null) product.setUnitPrice(request.unitPrice());
        if (request.description() != null) product.setDescription(request.description());
        if (request.category() != null) product.setCategory(request.category());
        if (request.stock() != null) product.setStock(request.stock());

        return productRepository.save(product);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable String productId) {
        productRepository.deleteById(productId);
    }
}
