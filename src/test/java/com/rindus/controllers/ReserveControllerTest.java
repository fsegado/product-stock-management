package com.rindus.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rindus.dto.ReserveDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.model.Cart;
import com.rindus.model.ReserveProduct;
import com.rindus.services.ReserveService;

@RunWith(MockitoJUnitRunner.class)
public class ReserveControllerTest {

	private ReserveController reserveController;

    @Mock
    private ReserveService reserveServiceMock;

    @Before
    public void init() {
    	reserveController = new ReserveController();
    	reserveController.setReserveService(reserveServiceMock);

        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @After
    public void teardown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void addReserve() throws ElementNotFoundException, NoAvailableException {

        when(reserveServiceMock.addReserve(Matchers.anyLong(), Matchers.any(ReserveDto.class))).thenReturn(buildCart());
        
        Cart expectedCart = buildCart();

        ResponseEntity<?> response = reserveController.addReserve(1L, buildReserveDto());

        Cart cartResponse = (Cart) response.getBody();

        List<ReserveProduct> reservedProductsResponse = new ArrayList<>(cartResponse.getReservedProducts());
        List<ReserveProduct> reservedProductsExpected = new ArrayList<>(expectedCart.getReservedProducts());
        
        assertThat(reservedProductsResponse.get(0).getAmount(), is(reservedProductsExpected.get(0).getAmount()));
        assertThat(reservedProductsResponse.get(0).getProductId(), is(reservedProductsExpected.get(0).getProductId()));

        verify(reserveServiceMock, times(1)).addReserve(Matchers.anyLong(), Matchers.any(ReserveDto.class));
    }
    
    @Test
    public void addReserveElementNotFoundException() throws ElementNotFoundException, NoAvailableException {

        doThrow(new ElementNotFoundException("ERROR")).when(reserveServiceMock).addReserve(Matchers.anyLong(), Matchers.any(ReserveDto.class));

        reserveController.addReserve(1L, buildReserveDto());

        verify(reserveServiceMock, times(1)).addReserve(Matchers.anyLong(), Matchers.any(ReserveDto.class));

    }
    
    @Test
    public void addReserveNoAvailableException() throws ElementNotFoundException, NoAvailableException {

        doThrow(new NoAvailableException("ERROR")).when(reserveServiceMock).addReserve(Matchers.anyLong(), Matchers.any(ReserveDto.class));

        reserveController.addReserve(1L, buildReserveDto());

        verify(reserveServiceMock, times(1)).addReserve(Matchers.anyLong(), Matchers.any(ReserveDto.class));

    }
    
    private Cart buildCart() {
    	Cart cart = new Cart();
    	ReserveProduct reserveProduct = new ReserveProduct();
    	reserveProduct.setAmount(1);
    	reserveProduct.setProductId(1);
    	cart.addReservedProduct(reserveProduct);
    	return cart;
    }
    
    private ReserveDto buildReserveDto() {
    	ReserveDto reserveDto = new ReserveDto();
    	reserveDto.setAmount(1);
    	reserveDto.setProductId(1);
    	return reserveDto;
    }

}