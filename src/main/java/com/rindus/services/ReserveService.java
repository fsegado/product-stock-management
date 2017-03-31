package com.rindus.services;

import com.rindus.dto.ReserveDto;
import com.rindus.exceptions.ElementNotFoundException;
import com.rindus.exceptions.NoAvailableException;
import com.rindus.model.Cart;

public interface ReserveService {

	Cart addReserve(long cartId, ReserveDto reserveDto) throws ElementNotFoundException, NoAvailableException;
}
