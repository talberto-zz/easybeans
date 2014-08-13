# Easybeans ATG module

This module provides the Nucleus components needed to use Easybeans in ATG.

## Configure the EntityManager

Create a Nucleus component with the following configuration at, for example, /easybeans/EntityManager

```
# EntityManager.properties
$scope=global
$class=com.github.talberto.easybeans.atg.NucleusEntityManager

# Inject a reference to the TransactionManager into the EntityManager
transactionManager=/atg/dynamo/transaction/TransactionManager
```

## Use the EntityManager

1. Inject the EntityManager into your own custom component

```
# MyComponent.properties

entityManager=/easybeans/EntityManager

```

2. Use it!

```java
EntityManager em = getEntityManager();

Product product = em.find(Product.class, "pd1000");
```