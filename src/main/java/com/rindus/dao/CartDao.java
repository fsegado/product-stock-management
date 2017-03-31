package com.rindus.dao;

import com.rindus.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CartDao extends CrudRepository<Cart, Long> {
}
