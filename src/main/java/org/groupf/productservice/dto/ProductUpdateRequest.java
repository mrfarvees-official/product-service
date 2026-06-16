package org.groupf.productservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ProductUpdateRequest(
        String name,

        @Positive(message = "Unit price must be greater than 0")
        Double unitPrice,

        String description,

        String category,

        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock
) {
}