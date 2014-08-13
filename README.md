# Easybeans Project

## Motivation

Does this seem similar to you?

```java
// Retrieve a product from the repository and populate a bean product with it
RepositoryItem productItem = productRepository.getItem("productId");
Product productBean = new Product();

product.setId(productItem.getRepositoryId());
product.setName(productItem.getPropertyValue("name"));
product.setDescription(productItem.getPropertyValue("description"));

// Same for every other property of the item Product!
```

Have you ever seen yourself coding hundreds and hundreds of lines like that often? Do you think, as I do, that there are too much boilerplate code there? For a product with 10 properties it may not seem 
hard to code but, what if the product has 50 properties? Why the ATG developers have to code hundreds of lines like that by hand? What if you could write just one line, much more like JPA style?

```java
EntityManager em = getAReferenceToEntityManager();
Product productBean = em.find(Product.class, "productId");
```

That's much more simple, elegant, efficient and less error prone, isn't it? That's the purpose of Easybeans, to free the ATG developer of coding by hand the CRUD operations related
to ATG repositories.
