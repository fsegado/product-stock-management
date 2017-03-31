package com.rindus.model;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;

@Entity
public class Product extends ResourceSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column
    private int stockAmount;

	@Column
	private int reservations;

	public int getStockAmount() {

		return stockAmount;
	}

	public void setStockAmount(int stockAmount) {

		this.stockAmount = stockAmount;
	}

	public int getReservations() {
		return reservations;
	}

	public void setReservations(int reservations) {
		this.reservations = reservations;
	}
}
