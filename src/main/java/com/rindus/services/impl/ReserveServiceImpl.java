package com.rindus.services.impl;

import com.rindus.dao.CartDao;
import com.rindus.dao.ProductDao;
import com.rindus.dto.ReserveDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.mappers.ReserveMapper;
import com.rindus.model.Cart;
import com.rindus.model.Product;
import com.rindus.model.ReserveProduct;
import com.rindus.services.ReserveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class ReserveServiceImpl implements ReserveService {

	@Autowired
	private CartDao cartDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ReserveMapper mapper;

	/**
	 * Add a reserve in the cart.
	 * @param cartId cart id
	 * @param reserveDto reserve information
	 * @return Cart with all the reservations
	 * @throws ElementNotFoundException
	 * @throws NoAvailableException
	 */
	@Override public Cart addReserve(long cartId, ReserveDto reserveDto) throws ElementNotFoundException, NoAvailableException {

		Assert.notNull(reserveDto, "ReserveDto is null");

		Cart cartUpdated;

		Cart cart = cartDao.findOne(cartId);

		if (cart != null) {

			Product product = productDao.findOne(reserveDto.getProductId());

			checkStock(product, reserveDto.getProductId(), reserveDto.getAmount());

			reserveProduct(product, reserveDto.getAmount());
			
			cart.addReservedProduct(mapper.toReserveProduct(reserveDto));
			cartUpdated = cartDao.save(cart);

		} else {
			throw new ElementNotFoundException("Cart "+ cartId + " not found");
		}

		log.info("Reserve {} added", reserveDto);

		return cartUpdated;
	}

	/**
	 * Check is the stock is available.
	 * @param product product to check the stock
	 * @param productId product id
	 * @param reserveAmount reserve amount
	 * @throws ElementNotFoundException
	 * @throws NoAvailableException
	 */
	private void checkStock(Product product, long productId, int reserveAmount) throws ElementNotFoundException,
			NoAvailableException {

		if (product == null) {

			throw new ElementNotFoundException("Product " + productId + " not found");

		} else if (reserveAmount > product.getStockAmount()) {
			throw new NoAvailableException("Product stock " + reserveAmount + " not available");
		}
	}

	/**
	 * Reserve the product.
	 * @param product product to reserve
	 * @param amount amount the products to reserve
	 */
	private void reserveProduct(Product product, int amount) {

		product.setStockAmount(product.getStockAmount() - amount);
		product.setReservations(product.getReservations() + 1);
		productDao.save(product);
	}

	public void setCartDao(CartDao cartDao) {
		this.cartDao = cartDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setMapper(ReserveMapper mapper) {
		this.mapper = mapper;
	}

	
}
