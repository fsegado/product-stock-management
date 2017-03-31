package com.rindus.controllers;

import com.rindus.dto.ReserveDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.model.Cart;
import com.rindus.services.ReserveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/carts/{cartId}")
@Slf4j
public class ReserveController {

	@Autowired
	private ReserveService reserveService;

	@RequestMapping(path = "/reserve", method = RequestMethod.POST)
	ResponseEntity<?> addReserve(@PathVariable long cartId, @RequestBody ReserveDto reserveDto) {

		log.info("Adding reserve {} for the cart id {}", reserveDto, cartId);

		Cart cart;
		try {
			cart = reserveService.addReserve(cartId, reserveDto);
			cart.add(linkTo(methodOn(ReserveController.class).addReserve(cartId, reserveDto)).withSelfRel());
		} catch (ElementNotFoundException e) {
			log.error("Unable add reserve {}, cart {} not found", reserveDto, cartId);
			return new ResponseEntity(e.getErrorMessage(), HttpStatus.NOT_FOUND);
		}
		catch (NoAvailableException e) {
			log.error("Reserve {} not valid, stock no available", reserveDto);
			return new ResponseEntity(e.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(cart);
	}

	public void setReserveService(ReserveService reserveService) {
		this.reserveService = reserveService;
	}

	
}
