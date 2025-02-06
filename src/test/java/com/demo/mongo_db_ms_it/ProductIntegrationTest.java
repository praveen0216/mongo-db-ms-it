package com.demo.mongo_db_ms_it;


import com.demo.mongo_db_ms_it.model.Product;
import com.demo.mongo_db_ms_it.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.containers.MongoDBContainer;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setup() {
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(1200.0);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", not(emptyOrNullString())))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200.0));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product();
        product.setName("Phone");
        product.setPrice(800.0);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/products/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.price").value(800.0));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setName("Tablet");
        product.setPrice(500.0);
        Product savedProduct = productRepository.save(product);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Tablet");
        updatedProduct.setPrice(600.0);

        mockMvc.perform(put("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Tablet"))
                .andExpect(jsonPath("$.price").value(600.0));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setName("Smartwatch");
        product.setPrice(250.0);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(delete("/products/" + savedProduct.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/products/" + savedProduct.getId()))
                .andExpect(status().isOk());
    }
}
