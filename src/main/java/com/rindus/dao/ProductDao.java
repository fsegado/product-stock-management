package com.rindus.dao;

import com.rindus.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProductDao extends CrudRepository<Product, Long> {
}
