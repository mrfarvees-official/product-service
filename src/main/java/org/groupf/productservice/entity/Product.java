package org.groupf.productservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.groupf.productservice.annotation.PrefixedId;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @PrefixedId(prefix = "product_", sequenceName = "product_seq")
    @Column(name = "product_id", length = 20, nullable = false, updatable = false)
    private String productId;

    @Column(nullable = false)
    private String name;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    private String description;

    private String category;

    private Integer stock;
}
