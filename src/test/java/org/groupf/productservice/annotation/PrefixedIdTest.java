package org.groupf.productservice.annotation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrefixedIdTest {

    @Nested
    class TestProduct {
        @PrefixedId(
                prefix = "PROD",
                sequenceName = "product_seq",
                numberLength = 4
        )
        private String productId;

        @Test
        void showReadPrefixedIdAnnotationValues() throws Exception {
            Field field = TestProduct.class.getDeclaredField("productId");

            PrefixedId prefixedId = field.getAnnotation(PrefixedId.class);

            assertNotNull(prefixedId);
            assertEquals("PROD", prefixedId.prefix());
            assertEquals("product_seq", prefixedId.sequenceName());
            assertEquals(4, prefixedId.numberLength());
        }
    }
}
