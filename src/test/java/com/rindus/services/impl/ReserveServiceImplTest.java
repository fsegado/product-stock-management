package com.rindus.services.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rindus.dao.CartDao;
import com.rindus.dao.ProductDao;
import com.rindus.dto.ReserveDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.mappers.ReserveMapper;
import com.rindus.model.Cart;
import com.rindus.model.Product;
import com.rindus.model.ReserveProduct;

@RunWith(MockitoJUnitRunner.class)
public class ReserveServiceImplTest {

	private ReserveServiceImpl reserveServiceImpl;
	
	@Mock
	private CartDao cartDaoMock;

	@Mock
	private ProductDao productDaoMock;

	@Mock
	private ReserveMapper mapperMock;
	
    @Before
    public void init() {
    	reserveServiceImpl = new ReserveServiceImpl();
    	reserveServiceImpl.setCartDao(cartDaoMock);
    	reserveServiceImpl.setProductDao(productDaoMock);
    	reserveServiceImpl.setMapper(mapperMock);

    }
    
    @Test
    public void addReserve() throws ElementNotFoundException, NoAvailableException {

    	when(cartDaoMock.findOne(Matchers.anyLong())).thenReturn(buildCart());
    	when(productDaoMock.findOne(Matchers.anyLong())).thenReturn(buildProduct());
    	when(mapperMock.toReserveProduct(Matchers.any(ReserveDto.class))).thenReturn(buildReserveProduct());
    	when(cartDaoMock.save(Matchers.any(Cart.class))).thenReturn(buildCartUpdated());
    	
    	Cart expectedCart = buildCartUpdated();
    	
    	Cart cartResponse = reserveServiceImpl.addReserve(1, buildReserveDto());
    	
        List<ReserveProduct> reservedProductsResponse = new ArrayList<>(cartResponse.getReservedProducts());
        List<ReserveProduct> reservedProductsExpected = new ArrayList<>(expectedCart.getReservedProducts());
        
        assertThat(reservedProductsResponse.get(0).getAmount(), is(reservedProductsExpected.get(0).getAmount()));
        assertThat(reservedProductsResponse.get(0).getProductId(), is(reservedProductsExpected.get(0).getProductId()));

        verify(cartDaoMock, times(1)).findOne(Matchers.anyLong());
        verify(productDaoMock, times(1)).findOne(Matchers.anyLong());
        verify(mapperMock, times(1)).toReserveProduct(Matchers.any(ReserveDto.class));
        verify(cartDaoMock, times(1)).save(Matchers.any(Cart.class));
    }

    @Test(expected = ElementNotFoundException.class)
    public void addReserveCartElementNotFoundException() throws ElementNotFoundException, NoAvailableException {

    	when(cartDaoMock.findOne(Matchers.anyLong())).thenReturn(null);

    	reserveServiceImpl.addReserve(1, buildReserveDto());

    	verify(cartDaoMock, times(1)).findOne(Matchers.anyLong());

    }
    
    @Test(expected = ElementNotFoundException.class)
    public void addReserveProductElementNotFoundException() throws ElementNotFoundException, NoAvailableException {

    	when(cartDaoMock.findOne(Matchers.anyLong())).thenReturn(buildCart());
    	when(productDaoMock.findOne(Matchers.anyLong())).thenReturn(null);

    	reserveServiceImpl.addReserve(1, buildReserveDto());

    	verify(cartDaoMock, times(1)).findOne(Matchers.anyLong());
    	verify(productDaoMock, times(1)).findOne(Matchers.anyLong());

    }
    

    @Test(expected = NoAvailableException.class)
    public void addReserveNoAvailableException() throws ElementNotFoundException, NoAvailableException {

    	when(cartDaoMock.findOne(Matchers.anyLong())).thenReturn(buildCart());
    	when(productDaoMock.findOne(Matchers.anyLong())).thenReturn(buildProduct());

    	reserveServiceImpl.addReserve(1, buildReserveDtoAmount3());

    	verify(cartDaoMock, times(1)).findOne(Matchers.anyLong());
    	verify(productDaoMock, times(1)).findOne(Matchers.anyLong());

    }
    private Cart buildCart() {
    	return new Cart();
    }
    
    private Cart buildCartUpdated() {
    	Cart cart = new Cart();
    	ReserveProduct reserveProduct = new ReserveProduct();
    	reserveProduct.setAmount(1);
    	reserveProduct.setProductId(1);
    	cart.addReservedProduct(reserveProduct);
    	return cart;
    }
    
    private Product buildProduct() {
        Product product = new Product();
        product.setStockAmount(1);
        return product;
    }

    private ReserveDto buildReserveDto() {
    	ReserveDto reserveDto = new ReserveDto();
    	reserveDto.setAmount(1);
    	reserveDto.setProductId(1);
    	return reserveDto;
    }
    
    private ReserveDto buildReserveDtoAmount3() {
    	ReserveDto reserveDto = new ReserveDto();
    	reserveDto.setAmount(3);
    	reserveDto.setProductId(1);
    	return reserveDto;
    }
    
    private ReserveProduct buildReserveProduct() {
    	
    	ReserveProduct reserveProduct = new ReserveProduct();
    	reserveProduct.setAmount(1);
    	reserveProduct.setProductId(1);
    	return reserveProduct;
    }
}
