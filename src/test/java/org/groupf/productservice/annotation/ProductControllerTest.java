package org.groupf.productservice.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.groupf.productservice.controller.ProductController;
import org.groupf.productservice.dto.ProductCreateRequest;
import org.groupf.productservice.dto.ProductUpdateRequest;
import org.groupf.productservice.entity.Product;
import org.groupf.productservice.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create product")
    void createProduct_shouldReturnSavedProduct() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(
                "Laptop",
                250000.00,
                "Gaming laptop",
                "Electronics",
                10
        );

        Product savedProduct = new Product(
                "product_1",
                "Laptop",
                250000.00,
                "Gaming laptop",
                "Electronics",
                10
        );

        Mockito.when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("product_1"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.unitPrice").value(250000.00))
                .andExpect(jsonPath("$.description").value("Gaming laptop"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.stock").value(10));

        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should return all products")
    void getAllProducts_shouldReturnProductList() throws Exception {
        Product product1 = new Product(
                "product_1",
                "Laptop",
                250000.00,
                "Gaming laptop",
                "Electronics",
                10
        );

        Product product2 = new Product(
                "product_2",
                "Mouse",
                5000.00,
                "Wireless mouse",
                "Accessories",
                50
        );

        Mockito.when(productRepository.findAll())
                .thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value("product_1"))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].productId").value("product_2"))
                .andExpect(jsonPath("$[1].name").value("Mouse"));

        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Should return product by id")
    void getProductById_shouldReturnProduct() throws Exception {
        Product product = new Product(
                "product_1",
                "Laptop",
                250000.00,
                "Gaming laptop",
                "Electronics",
                10
        );

        Mockito.when(productRepository.findById("product_1"))
                .thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/product_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("product_1"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.unitPrice").value(250000.00))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.stock").value(10));

        verify(productRepository).findById("product_1");
    }

    @Test
    @DisplayName("Should update product")
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {
        Product existingProduct = new Product(
                "product_1",
                "Old Laptop",
                200000.00,
                "Old description",
                "Old Category",
                5
        );

        Product updatedProduct = new Product(
                "product_1",
                "Updated Laptop",
                275000.00,
                "Updated gaming laptop",
                "Electronics",
                15
        );

        ProductUpdateRequest request = new ProductUpdateRequest(
                "Updated Laptop",
                275000.00,
                "Updated gaming laptop",
                "Electronics",
                15
        );

        Mockito.when(productRepository.findById("product_1"))
                .thenReturn(Optional.of(existingProduct));

        Mockito.when(productRepository.save(any(Product.class)))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/products/product_1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("product_1"))
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.unitPrice").value(275000.00))
                .andExpect(jsonPath("$.description").value("Updated gaming laptop"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.stock").value(15));

        verify(productRepository).findById("product_1");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product")
    void deleteProduct_shouldDeleteProductById() throws Exception {
        mockMvc.perform(delete("/products/product_1"))
                .andExpect(status().isOk());

        verify(productRepository).deleteById("product_1");
    }

    @Test
    @DisplayName("Should return bad request when product name is blank")
    void createProduct_withBlankName_shouldReturnBadRequest() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(
                "",
                250000.00,
                "Gaming laptop",
                "Electronics",
                10
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request when unit price is invalid")
    void createProduct_withInvalidUnitPrice_shouldReturnBadRequest() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(
                "Laptop",
                -100.00,
                "Gaming laptop",
                "Electronics",
                10
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request when stock is negative")
    void createProduct_withNegativeStock_shouldReturnBadRequest() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(
                "Laptop",
                250000.00,
                "Gaming laptop",
                "Electronics",
                -1
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}