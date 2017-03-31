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
import org.springframework.http.ResponseEntity;

import com.rindus.dao.ProductDao;
import com.rindus.dto.ProductStockDto;
import com.rindus.dto.ReserveDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.model.Cart;
import com.rindus.model.Product;
import com.rindus.model.ReserveProduct;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplTest {

	private StockServiceImpl stockServiceImpl;
	
	@Mock
	private ProductDao productDaoMock;
	
    @Before
    public void init() {
    	stockServiceImpl = new StockServiceImpl();
    	stockServiceImpl.setProductDao(productDaoMock);

    }
    
    @Test
    public void addProductStock() throws ElementNotFoundException, NoAvailableException {

    	
        when(productDaoMock.findOne(Matchers.anyLong())).thenReturn(buildProductStockAmount1());
        when(productDaoMock.save(Matchers.any(Product.class))).thenReturn(buildProductStockAmount2());
        
        Product expectedProduct = buildProductStockAmount2();

        Product response = stockServiceImpl.addProductStock(1L, buildProductStockDto());

        assertThat(response.getStockAmount(), is(expectedProduct.getStockAmount()));

        verify(productDaoMock, times(1)).findOne(Matchers.anyLong());
        verify(productDaoMock, times(1)).save(Matchers.any(Product.class));
    }
    
    @Test(expected = ElementNotFoundException.class)
    public void addProductStockElementNotFoundException() throws ElementNotFoundException {

    	when(productDaoMock.findOne(Matchers.anyLong())).thenReturn(null);

        stockServiceImpl.addProductStock(1L, buildProductStockDto());

        verify(productDaoMock, times(1)).findOne(Matchers.anyLong());

    }
    
    @Test
    public void removeProductStock() throws ElementNotFoundException, NoAvailableException {

    	
        when(productDaoMock.findOne(Matchers.anyLong())).thenReturn(buildProductStockAmount2());
        when(productDaoMock.save(Matchers.any(Product.class))).thenReturn(buildProductStockAmount1());
        
        Product expectedProduct = buildProductStockAmount1();

        Product response = stockServiceImpl.removeProductStock(1L, buildProductStockDto());

        assertThat(response.getStockAmount(), is(expectedProduct.getStockAmount()));

        verify(productDaoMock, times(1)).findOne(Matchers.anyLong());
        verify(productDaoMock, times(1)).save(Matchers.any(Product.class));
    }
    
    @Test(expected = ElementNotFoundException.class)
    public void removeProductStockElementNotFoundException() throws ElementNotFoundException, NoAvailableException {

    	when(productDaoMock.findOne(Matchers.anyLong())).thenReturn(null);

        stockServiceImpl.removeProductStock(1L, buildProductStockDto());

        verify(productDaoMock, times(1)).findOne(Matchers.anyLong());

    }
    
    @Test(expected = NoAvailableException.class)
    public void removeProductStockNoAvailableException() throws ElementNotFoundException, NoAvailableException {

    	when(productDaoMock.findOne(Matchers.anyLong())).thenReturn(buildProductStockAmount2());

        stockServiceImpl.removeProductStock(1L, buildProductStockDtoStock3());

        verify(productDaoMock, times(1)).findOne(Matchers.anyLong());

    }  
    
    private ProductStockDto buildProductStockDto() {
        ProductStockDto productStockDto = new ProductStockDto();
        productStockDto.setStock(1);
        return productStockDto;
    }
    
    private ProductStockDto buildProductStockDtoStock3() {
        ProductStockDto productStockDto = new ProductStockDto();
        productStockDto.setStock(3);
        return productStockDto;
    }
    
    private Product buildProductStockAmount1() {
        Product product = new Product();
        product.setStockAmount(1);
        return product;
    }
    
    private Product buildProductStockAmount2() {
        Product product = new Product();
        product.setStockAmount(2);
        return product;
    }
}
