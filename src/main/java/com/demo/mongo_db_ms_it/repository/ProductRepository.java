package com.demo.mongo_db_ms_it.repository;

import com.demo.mongo_db_ms_it.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
