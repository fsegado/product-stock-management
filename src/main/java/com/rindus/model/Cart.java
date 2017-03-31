package com.rindus.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.hateoas.ResourceSupport;

@Entity
public class Cart extends ResourceSupport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy="cart")
	private Collection<ReserveProduct> reservedProducts;

	public Collection<ReserveProduct> getReservedProducts() {
		return reservedProducts;
	}

	public void addReservedProduct(ReserveProduct reserveProduct) {
		if (reserveProduct != null) {
			if (reservedProducts == null) {
				reservedProducts = new ArrayList<ReserveProduct>();
			}
			this.reservedProducts.add(reserveProduct);
			reserveProduct.setCart(this);
		}
	}

}
