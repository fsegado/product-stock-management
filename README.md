# Product Stock Management

This application is a product stock management as a REST microservice and a an embedded H2 database (empty by default).

### Requirements

- Maven
- JDK 8

### Running

To build and start the server type

```sh
$ mvn spring-boot:run
```

from the root directory.

### Endpoints

Add a product stock

POST localhost:8080/products/{productId}/stocks

{
	"stock" : 2
}

Remove a product stock

DELETE localhost:8080/products/{productId}/stocks

{
	"stock" : 2
}

Reserve a product stock

POST localhost:8080/carts/1/reserve

{
	"productId" : 1,
	"amount" : 1
}


Also are available the CRUD for products and carts.

#Porducts

GET localhost:8080/products

POST localhost:8080/products

{
	"stockAmount" : 1
}

DELETE localhost:8080/products/{productId}

UPDATE localhost:8080/products/{productId}

{
	"stockAmount" : 2
}


#Carts

GET localhost:8080/carts

POST localhost:8080/carts

{}

DELETE localhost:8080/carts/{cartId}

UPDATE localhost:8080/carts/{cartId}



### Todo

 - Swagger
 - Remove reserve
 - Initialize the H2 Database with products and carts by default

