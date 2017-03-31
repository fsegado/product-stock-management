package com.rindus.services;

import com.rindus.dto.ProductStockDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.model.Product;

public interface StockService {

	Product addProductStock(Long productId, ProductStockDto productStockDto) throws ElementNotFoundException;

	Product removeProductStock(Long productId, ProductStockDto productStockDto) throws ElementNotFoundException, NoAvailableException;
}
