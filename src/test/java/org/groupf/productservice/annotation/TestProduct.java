package org.groupf.productservice.annotation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
class TestProduct {

    @Id
    @PrefixedId(
            prefix = "PROD",
            sequenceName = "product_seq",
            numberLength = 4
    )
    private String productId;
}