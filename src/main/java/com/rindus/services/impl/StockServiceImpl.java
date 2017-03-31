package com.rindus.services.impl;

import com.rindus.dao.ProductDao;
import com.rindus.dto.ProductStockDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.model.Product;
import com.rindus.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class StockServiceImpl implements StockService {

	@Autowired
	private ProductDao productDao;

	/**
	 * Add a product stock.
	 * 
	 * @param productId product id 
	 * @param productStockDto product stock with the information of the product stock
	 * @return Product the product with the stock updated
	 */
	@Override
	public Product addProductStock(Long productId, ProductStockDto productStockDto) throws ElementNotFoundException {

		Assert.notNull(productStockDto, "ProductStockDto is null");

		Product productUpdated;

		Product product = productDao.findOne(productId);

		if (product != null) {

			product.setStockAmount(product.getStockAmount() + productStockDto.getStock());
			productUpdated = productDao.save(product);
		} else {
			throw new ElementNotFoundException("Product "+ productId + " not found");
		}

		log.info("Product stock {} added", productStockDto);

		return productUpdated;
	}

	/**
	 * Remove a product stock.
	 * 
	 * @param productId product id 
	 * @param productStockDto product stock with the information of the product stock
	 * @return Product the product with the stock updated
	 */
	@Override
	public Product removeProductStock(Long productId, ProductStockDto productStockDto) throws ElementNotFoundException, NoAvailableException {

		Assert.notNull(productStockDto, "ProductStockDto is null");

		Product productUpdated;

		Product product = productDao.findOne(productId);

		if (product != null) {

			int currentStock = product.getStockAmount();
			
			if (productStockDto.getStock() > currentStock) {
				throw new NoAvailableException("Product stock " + productStockDto + " no available");
			}
			
			product.setStockAmount(product.getStockAmount() - productStockDto.getStock());
			productUpdated = productDao.save(product);
		} else {
			throw new ElementNotFoundException("Product "+ productId + " not found");
		}

		log.info("Product stock {} removed", productStockDto);

		return productUpdated;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	
}
