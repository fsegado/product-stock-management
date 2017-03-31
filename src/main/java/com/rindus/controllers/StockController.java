package com.rindus.controllers;

import com.rindus.dto.ProductStockDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.model.Product;
import com.rindus.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/products/{productId}")
@Slf4j
public class StockController {

	@Autowired
	private StockService stockService;

    @RequestMapping(path = "/stocks", method = RequestMethod.POST)
	public ResponseEntity<?> addProductStock(@PathVariable long productId, @RequestBody ProductStockDto productStockDto) {
    	
    	log.info("Adding product stock {} for the product id {}", productStockDto, productId);

		Product product = null;
		try {
			product = stockService.addProductStock(productId, productStockDto);
			product.add(linkTo(methodOn(StockController.class).addProductStock(productId, productStockDto)).withSelfRel());
		} catch (ElementNotFoundException e) {
			log.error("Unable add product stock {}, product {} not found", productStockDto, productId);
			return new ResponseEntity(e.getErrorMessage(), HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(product);
    }

	@RequestMapping(path = "/stocks", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeProductStock(@PathVariable long productId, @RequestBody ProductStockDto productStockDto) {

		log.info("Removing product stock {} for the product id {}", productStockDto, productId);

		Product product = null;
		try {
			product = stockService.removeProductStock(productId, productStockDto);
			product.add(linkTo(methodOn(StockController.class).removeProductStock(productId, productStockDto)).withSelfRel());
		} catch (ElementNotFoundException e) {
			log.error("Unable remove product stock {}, product {} not found", productStockDto, productId);
			return new ResponseEntity(e.getErrorMessage(), HttpStatus.NOT_FOUND);
			
		} catch (NoAvailableException e) {
			log.error("Product stock {} no available for the product {}", productStockDto, productId);
			return new ResponseEntity(e.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(product);
	}

	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}
}