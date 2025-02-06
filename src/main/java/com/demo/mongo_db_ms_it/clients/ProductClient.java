package com.demo.mongo_db_ms_it.clients;

import com.demo.mongo_db_ms_it.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "elastic-db-ms-it", url = "http://localhost:8080/api")
public interface ProductClient {
    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable String id);
}
