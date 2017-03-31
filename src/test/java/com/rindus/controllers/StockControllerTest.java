package com.rindus.controllers;

import com.rindus.controllers.StockController;
import com.rindus.dto.ProductStockDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.model.Product;
import com.rindus.services.StockService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StockControllerTest {

    private StockController stockController;

    @Mock
    private StockService stockServiceMock;

    @Before
    public void init() {
        stockController = new StockController();
        stockController.setStockService(stockServiceMock);

        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);

    }

    @After
    public void teardown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void addProductStock() throws ElementNotFoundException {

        when(stockServiceMock.addProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class))).thenReturn(buildProduct());

        Product expectedProduct = buildProduct();

        ResponseEntity<?> response = stockController.addProductStock(1, buildProductStockDto());

        Product productResponse = (Product) response.getBody();

        assertThat(productResponse.getStockAmount(), is(expectedProduct.getStockAmount()));

        verify(stockServiceMock, times(1)).addProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class));
    }

    @Test
    public void addProductStockElementNotFoundException() throws ElementNotFoundException {

        doThrow(new ElementNotFoundException("ERROR")).when(stockServiceMock).addProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class));

        stockController.addProductStock(1, buildProductStockDto());

        verify(stockServiceMock, times(1)).addProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class));

    }

    @Test
    public void removeProductStock() throws ElementNotFoundException, NoAvailableException {

        when(stockServiceMock.removeProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class))).thenReturn(buildProduct());

        Product expectedProduct = buildProduct();

        ResponseEntity<?> response = stockController.removeProductStock(1, buildProductStockDto());

        Product productResponse = (Product) response.getBody();

        assertThat(productResponse.getStockAmount(), is(expectedProduct.getStockAmount()));

        verify(stockServiceMock, times(1)).removeProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class));

    }

    @Test
    public void removeProductStockElementNotFoundException() throws ElementNotFoundException, NoAvailableException {

        doThrow(new ElementNotFoundException("ERROR")).when(stockServiceMock).removeProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class));

        stockController.removeProductStock(1, buildProductStockDto());

        verify(stockServiceMock, times(1)).removeProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class));

    }

    @Test
    public void removeProductStockNoAvailableException() throws ElementNotFoundException, NoAvailableException {

        doThrow(new NoAvailableException("ERROR")).when(stockServiceMock).removeProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class));

        stockController.removeProductStock(1, buildProductStockDto());

        verify(stockServiceMock, times(1)).removeProductStock(Matchers.anyLong(), Matchers.any(ProductStockDto.class));

    }

    private ProductStockDto buildProductStockDto() {
        ProductStockDto productStockDto = new ProductStockDto();
        productStockDto.setStock(1);
        return productStockDto;
    }

    private Product buildProduct() {
        Product product = new Product();
        product.setStockAmount(1);
        return product;
    }
}
